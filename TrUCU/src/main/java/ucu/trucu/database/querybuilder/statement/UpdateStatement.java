package ucu.trucu.database.querybuilder.statement;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import ucu.trucu.database.querybuilder.CaseExpression;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class UpdateStatement implements Statement {

    private static final String UPDATE = "UPDATE %s";
    private static final String SET = "SET %s";
    private static final String WHERE = "WHERE %s";
    private static final String FROM = "FROM %s";
    private static final String INNER_JOIN = "INNER JOIN %s ON %s";

    private final String table;
    private final Map<String, Object> sets = new HashMap<>();
    private final Map<String, String> joinTables = new HashMap<>();
    private String filter;

    public UpdateStatement(String table) {
        this.table = table;
    }

    public UpdateStatement set(String key, Function<CaseExpression, CaseExpression> caseBuilder) {
        sets.put(key, caseBuilder.apply(new CaseExpression()).build());
        return this;
    }

    public UpdateStatement set(String key, Object newValue) {
        sets.put(key, String.format("'%s'", newValue));
        return this;
    }

    public UpdateStatement set(Map<String, Object> newValues) {
        newValues.forEach(this::set);
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
            statement += StringUtils.SPACE + String.format(SET, StringUtils.join(StringUtils.COMA, sets.entrySet(), set -> String.format("%s = %s", set.getKey(), set.getValue())));
        }
        if (!joinTables.isEmpty()) {
            statement += StringUtils.SPACE + String.format(FROM, table);
            statement += StringUtils.SPACE + StringUtils.join(StringUtils.SPACE, joinTables.entrySet(), entry -> String.format(INNER_JOIN, entry.getKey(), entry.getValue()));
        }
        if (filter != null) {
            statement += StringUtils.SPACE + String.format(WHERE, filter);
        }

        return statement;
    }
}
