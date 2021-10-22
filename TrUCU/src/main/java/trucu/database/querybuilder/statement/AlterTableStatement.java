package trucu.database.querybuilder.statement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import trucu.database.SQLType;
import trucu.database.querybuilder.TableColumn;
import trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class AlterTableStatement implements Statement {

    private static final String ALTER_TABLE = "ALTER TABLE %s";
    private static final String ADD_COLUMN = "ADD \n%s";
    private static final String ALTER_COLUMN = "ALTER COLUMN %s";
    private static final String DROP = "DROP COLUMN %s";

    private final String tableName;
    private final List<TableColumn> newColumns = new LinkedList<>();
    private TableColumn alteredColumn;
    private final List<String> droppedColumns = new LinkedList<>();

    public AlterTableStatement(String tableName) {
        this.tableName = tableName;
    }

    public AlterTableStatement addColumn(String name, SQLType type) {
        return addColumn(name, type, null);
    }

    public AlterTableStatement addColumn(String name, SQLType type, Integer presition) {
        newColumns.add(new TableColumn(name, type, presition));
        return this;
    }

    public AlterTableStatement alterColumn(String name, SQLType newType) {
        return alterColumn(name, newType, null);
    }

    public AlterTableStatement alterColumn(String name, SQLType newType, Integer newPresition) {
        alteredColumn = new TableColumn(name, newType, newPresition);
        return this;
    }

    public AlterTableStatement alterColumn(String name, SQLType newType, Integer newPresition, boolean notNull) {
        alteredColumn = new TableColumn(name, newType, newPresition);
        alteredColumn.setNotNull(notNull);
        return this;
    }

    public AlterTableStatement dropColumns(String... tables) {
        droppedColumns.addAll(Arrays.asList(tables));
        return this;
    }

    @Override
    public String build() {
        String statement = String.format(ALTER_TABLE, tableName);

        if (!newColumns.isEmpty()) {
            statement += StringUtils.SPACE + String.format(ADD_COLUMN, StringUtils.join(StringUtils.COMA_LN, newColumns, TableColumn::toTableDefinitionFormat));
        }
        if (alteredColumn != null) {
            statement += StringUtils.SPACE + String.format(ALTER_COLUMN, alteredColumn.toTableDefinitionFormat());
        }
        if (!droppedColumns.isEmpty()) {
            statement += StringUtils.SPACE + String.format(DROP, StringUtils.join(StringUtils.COMA, droppedColumns));
        }
        return statement;
    }
}
