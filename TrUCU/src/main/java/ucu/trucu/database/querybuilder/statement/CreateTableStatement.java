package ucu.trucu.database.querybuilder.statement;

import ucu.trucu.database.querybuilder.ForeignTableKey;
import ucu.trucu.database.querybuilder.TableColumn;
import ucu.trucu.database.querybuilder.Filter;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import ucu.trucu.database.querybuilder.SQLType;
import ucu.trucu.database.querybuilder.ForeignTableKey.ON_DELETE_ACTION;
import ucu.trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class CreateTableStatement implements Statement {

    private final static String CREATE_TABLE = "CREATE TABLE %s \n(%s)";
    private final static String CONSTRAINT_PK = "CONSTRAINT PK_%s PRIMARY KEY (%s)";
    private final static String CONSTRAINT_UQ = "CONSTRAINT UQ_%s UNIQUE (%s)";
    private final static String CHECK_CONSTRAIN = "CHECK (%s)";

    private final String tableName;
    private String[] pkColumns;
    private final List<TableColumn> columns = new LinkedList<>();
    private final List<String> uniqueKeys = new LinkedList<>();
    private final List<ForeignTableKey> foreignKeys = new LinkedList<>();
    private String check;

    public CreateTableStatement(String tableName) {
        this.tableName = tableName;
    }

    public CreateTableStatement addColumn(String name, SQLType type) {
        return addColumn(name, type, null);
    }

    public CreateTableStatement addColumn(String name, SQLType type, Integer presition) {
        columns.add(new TableColumn(name, type, presition));
        return this;
    }

    public CreateTableStatement setForeignKey(String name, String reference, String otherTable) {
        return setForeignKey(name, reference, otherTable, null);
    }

    public CreateTableStatement setForeignKey(String name, String reference, String otherTable, ON_DELETE_ACTION deleteAction) {
        return setForeignKey(new String[]{name}, new String[]{reference}, otherTable, deleteAction);
    }

    public CreateTableStatement setForeignKey(String[] names, String[] references, String otherTable) {
        return setForeignKey(names, references, otherTable, null);
    }

    public CreateTableStatement setForeignKey(String[] names, String[] references, String otherTable, ON_DELETE_ACTION deleteAction) {
        List<TableColumn> keys = new LinkedList<>();
        for (String name : names) {
            keys.add(findColumn(name).orElseThrow(() -> new IllegalArgumentException("Columna FK no existente: " + name)));
        }
        foreignKeys.add(new ForeignTableKey(keys.toArray(new TableColumn[0]), references, otherTable, deleteAction));
        return this;
    }

    public CreateTableStatement setCheck(String filter) {
        this.check = filter;
        return this;
    }

    public CreateTableStatement setCheck(Filter filter) {
        return setCheck(filter.toString());
    }

    public CreateTableStatement setCheck(Function<Filter, String> filter) {
        return setCheck(new Filter(filter).toString());
    }

    public CreateTableStatement setPrimaryKey(String... pkColumns) {
        this.pkColumns = pkColumns;
        return this;
    }

    public CreateTableStatement setUniqueKey(String... uniqueColumns) {
        this.uniqueKeys.add(StringUtils.join(StringUtils.COMA, uniqueColumns));
        return this;
    }

    public CreateTableStatement setNotNull(String columnName, boolean notNull) {
        findColumn(columnName).ifPresent(column -> column.setNotNull(notNull));
        return this;
    }

    public CreateTableStatement setDefaultValue(String columnName, Object defaultValue) {
        findColumn(columnName).ifPresent(column -> column.setDefaultValue(defaultValue));
        return this;
    }

    private Optional<TableColumn> findColumn(String columnName) {
        return this.columns.stream()
                .filter(column -> column.getName().equals(columnName))
                .findFirst();
    }

    @Override
    public String build() {
        String tableConfig = StringUtils.join(StringUtils.COMA, columns, TableColumn::toTableDefinitionFormat);
        if (check != null) {
            tableConfig += StringUtils.COMA_LN + String.format(CHECK_CONSTRAIN, check);
        }
        if (pkColumns != null) {
            tableConfig += StringUtils.COMA_LN + StringUtils.join(StringUtils.COMA, pkColumns, key -> String.format(CONSTRAINT_PK, key.replace(StringUtils.COMA, "_"), key));
        }
        if (!uniqueKeys.isEmpty()) {
            tableConfig += StringUtils.COMA_LN + StringUtils.join(StringUtils.COMA, uniqueKeys, key -> String.format(CONSTRAINT_UQ, key.replace(StringUtils.COMA, "_"), key));
        }
        if (!foreignKeys.isEmpty()) {
            tableConfig += StringUtils.COMA_LN + StringUtils.join(StringUtils.COMA_LN, foreignKeys, ForeignTableKey::toCreateTableConstraintFormat);
        }

        return String.format(CREATE_TABLE, tableName, tableConfig);
    }
}
