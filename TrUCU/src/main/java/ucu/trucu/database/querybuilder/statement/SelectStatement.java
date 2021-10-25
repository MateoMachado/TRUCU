package ucu.trucu.database.querybuilder.statement;

import ucu.trucu.database.querybuilder.Filter;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import ucu.trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class SelectStatement implements Statement {

    private final static String SELECT_FROM = "SELECT %s FROM %s";
    private final static String WHERE = "WHERE %s";
    private final static String ORDER_BY = "ORDER BY %s";
    private final static String INNER_JOIN = "INNER JOIN %s ON %s";

    private final String table;
    private final String[] columns;
    private final Map<String, String> joinTables = new HashMap<>();
    private String filter;
    private String order;

    public SelectStatement(String table) {
        this.table = table;
        this.columns = new String[]{"*"};
    }

    public SelectStatement(String table, String[] columns) {
        this.table = table;
        this.columns = columns;
    }

    public String getTable() {
        return table;
    }

    public SelectStatement where(String filter) {
        this.filter = filter;
        return this;
    }

    public SelectStatement where(Filter filter) {
        return where(filter.toString());
    }

    public SelectStatement where(Function<Filter, String> filterBuilder) {
        return where(new Filter(filterBuilder).toString());
    }

    public SelectStatement orderAsc(String... columns) {
        this.order = StringUtils.join(StringUtils.COMA, columns) + " ASC";
        return this;
    }

    public SelectStatement orderDesc(String... columns) {
        this.order = StringUtils.join(StringUtils.COMA, columns) + " DESC";
        return this;
    }

    public SelectStatement joinOn(String otherTable, String on) {
        this.joinTables.put(otherTable, on);
        return this;
    }

    public SelectStatement joinOn(String otherTable, Filter on) {
        return joinOn(otherTable, on.toString());
    }

    public SelectStatement joinOn(String otherTable, Function<Filter, String> on) {
        return joinOn(otherTable, new Filter(on).toString());
    }

    @Override
    public String build() {
        String statement = String.format(SELECT_FROM, StringUtils.join(StringUtils.COMA, columns), table);

        if (!joinTables.isEmpty()) {
            statement += StringUtils.LN_TABBED + StringUtils.join(StringUtils.LN_TABBED, joinTables.entrySet(), entry -> String.format(INNER_JOIN, entry.getKey(), entry.getValue()));
        }
        if (filter != null) {
            statement += StringUtils.LN_TABBED + String.format(WHERE, filter);
        }
        if (order != null) {
            statement += StringUtils.LN_TABBED + String.format(ORDER_BY, order);
        }
        return statement;
    }
}
