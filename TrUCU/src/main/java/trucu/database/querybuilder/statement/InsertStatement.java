package trucu.database.querybuilder.statement;

import java.util.LinkedList;
import java.util.List;
import trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class InsertStatement implements Statement {

    private static final String INSERT = "INSERT INTO %s (%s)";
    private static final String VALUES = "VALUES \n%s";

    private final String tableName;
    private final List<Object[]> values = new LinkedList<>();
    private SelectStatement select;
    private String[] keys;

    public InsertStatement(String tableName) {
        this.tableName = tableName;
    }

    public InsertStatement keys(String... keys) {
        this.keys = keys;
        return this;
    }

    public InsertStatement values(List<Object[]> values) {
        this.values.addAll(values);
        return this;
    }

    public InsertStatement values(Object... values) {
        this.values.add(values);
        return this;
    }

    public InsertStatement select(SelectStatement select) {
        this.select = select;
        return this;
    }

    @Override
    public String build() {
        String statement = String.format(INSERT, tableName, StringUtils.join(StringUtils.COMA, keys));
        if (select != null) {
            statement += StringUtils.SPACE + select.build();
        } else {
            String rows = StringUtils.join(StringUtils.COMA_NEW_LINE, values, row -> String.format("(%s)", StringUtils.join(StringUtils.COMA, row)));
            statement += StringUtils.SPACE + String.format(VALUES, rows);
        }
        return statement;
    }
}
