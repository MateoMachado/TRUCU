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
    private final static String SELECT_DISTINCT_FROM = "SELECT DISTINCT %s FROM %s";
    private final static String WHERE = "WHERE %s";
    private final static String ORDER_BY = "ORDER BY %s";
    private final static String INNER_JOIN = "INNER JOIN %s ON %s";
    private final static String OFFSET = "OFFSET %s ROWS";
    private final static String FETCH_NEXT = "FETCH NEXT %s ROWS ONLY";

    private final String table;
    private final boolean distinct;
    private final String[] columns;
    private final Map<String, String> joinTables = new HashMap<>();
    private String filter;
    private String order;
    private Integer offset;
    private Integer fetchNext;

    public SelectStatement(String table, String... columns) {
        this.table = table;
        this.columns = columns != null && columns.length > 0 ? columns : new String[]{"*"};
        this.distinct = false;
    }

    public SelectStatement(String table, String[] columns, boolean distinct) {
        this.table = table;
        this.columns = columns != null && columns.length > 0 ? columns : new String[]{"*"};
        this.distinct = distinct;
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

    public SelectStatement joinOnId(String otherTable, String onId) {
        return joinOn(otherTable, String.format("%s.%s = %s.%s", table, onId, otherTable, onId));
    }

    public SelectStatement joinOn(String otherTable, Filter on) {
        return joinOn(otherTable, on.toString());
    }

    public SelectStatement joinOn(String otherTable, Function<Filter, String> on) {
        return joinOn(otherTable, new Filter(on).toString());
    }

    public SelectStatement offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SelectStatement fetchNext(int rows) {
        this.fetchNext = rows;
        return this;
    }

    @Override
    public String build() {
        String statement = "";
        if (distinct) {
            statement = String.format(SELECT_DISTINCT_FROM, StringUtils.join(StringUtils.COMA, columns), table);
        } else {
            statement = String.format(SELECT_FROM, StringUtils.join(StringUtils.COMA, columns), table);
        }

        if (!joinTables.isEmpty()) {
            statement += StringUtils.SPACE + StringUtils.join(StringUtils.LN_TABBED, joinTables.entrySet(), entry -> String.format(INNER_JOIN, entry.getKey(), entry.getValue()));
        }
        if (filter != null) {
            statement += StringUtils.SPACE + String.format(WHERE, filter);
        }
        if (order != null) {
            statement += StringUtils.SPACE + String.format(ORDER_BY, order);
        }
        if (offset != null) {
            statement += StringUtils.SPACE + String.format(OFFSET, offset);
        }
        if (fetchNext != null && fetchNext != 0) {
            statement += StringUtils.SPACE + String.format(FETCH_NEXT, fetchNext);
        }
        return statement;
    }
}
