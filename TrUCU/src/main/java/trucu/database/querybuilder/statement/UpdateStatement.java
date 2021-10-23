package trucu.database.querybuilder.statement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import trucu.database.querybuilder.Filter;
import trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class UpdateStatement implements Statement {

    private static final String UPDATE = "UPDATE %s";
    private static final String SET = "SET \n%s";
    private static final String WHERE = "WHERE %s";
    private static final String FROM = "FROM %s";
    private static final String INNER_JOIN = "INNER JOIN %s ON %s";

    private final String table;
    private final Map<String, Object> sets = new HashMap<>();
    private final Map<String, String> joinTables = new HashMap<>();
    private String from;
    private String filter;

    public UpdateStatement(String table) {
        this.table = table;
    }

    public UpdateStatement set(String key, Object newValue) {
        sets.put(key, newValue);
        return this;
    }

    public UpdateStatement where(String filter) {
        this.filter = filter;
        return this;
    }

    public UpdateStatement where(Filter filter) {
        return where(filter.toString());
    }

    public UpdateStatement where(Function<Filter, String> filterBuilder) {
        return where(new Filter(filterBuilder).toString());
    }

    public UpdateStatement from(String table) {
        this.from = table;
        return this;
    }

    public UpdateStatement joinOn(String table, String on) {
        joinTables.put(table, on);
        return this;
    }

    public UpdateStatement joinOn(String table, Filter on) {
        return joinOn(table, on.toString());
    }

    public String getTable() {
        return table;
    }

    @Override
    public String build() {
        String statement = String.format(UPDATE, table);

        if (!sets.isEmpty()) {
            statement += StringUtils.SPACE + String.format(SET, StringUtils.join(StringUtils.COMA_LN, sets.entrySet(), set -> String.format("%s = %s", set.getKey(), set.getValue())));
        }
        if (from != null) {
            statement += StringUtils.LN + String.format(FROM, from);

            if (!joinTables.isEmpty()) {
                statement += StringUtils.LN_TABBED + StringUtils.join(StringUtils.LN_TABBED, joinTables.entrySet(), entry -> String.format(INNER_JOIN, entry.getKey(), entry.getValue()));
            }
        }
        if (filter != null) {
            statement += StringUtils.LN + String.format(WHERE, filter);
        }

        return statement;
    }
}
