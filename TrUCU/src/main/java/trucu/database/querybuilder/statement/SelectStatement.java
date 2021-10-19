package trucu.database.querybuilder.statement;

import trucu.database.querybuilder.Filter;
import java.util.HashMap;
import java.util.Map;
import trucu.util.StringUtils;

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

    public SelectStatement where(String filter) {
        this.filter = filter;
        return this;
    }

    public SelectStatement where(Filter filter) {
        this.filter = filter.toString();
        return this;
    }

    public SelectStatement orderAsc(String... columns) {
        this.order = StringUtils.join(StringUtils.COMA, columns) + " ASC";
        return this;
    }

    public SelectStatement orderDesc(String... columns) {
        this.order = StringUtils.join(StringUtils.COMA, columns) + " DESC";
        return this;
    }

    public SelectStatement joinOn(String otherTable, Filter on) {
        joinOn(otherTable, on.toString());
        return this;
    }

    public SelectStatement joinOn(String otherTable, String on) {
        this.joinTables.put(otherTable, on);
        return this;
    }

    @Override
    public String build() {
        String statement = String.format(SELECT_FROM, StringUtils.join(StringUtils.COMA, columns), table);

        if (!joinTables.isEmpty()) {
            statement += StringUtils.NEW_LINE_TABBED + StringUtils.join(StringUtils.NEW_LINE_TABBED, joinTables.entrySet(), entry -> String.format(INNER_JOIN, entry.getKey(), entry.getValue()));
        }
        if (filter != null) {
            statement += StringUtils.NEW_LINE_TABBED + String.format(WHERE, filter);
        }
        if (order != null) {
            statement += StringUtils.NEW_LINE_TABBED + String.format(ORDER_BY, order);
        }
        return statement;
    }
}
