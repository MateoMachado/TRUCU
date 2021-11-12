package ucu.trucu.database.querybuilder.statement;

import java.util.function.Function;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class DeleteStatement implements Statement {

    private static final String DELETE_FROM = "DELETE FROM %s";
    private static final String WHERE = "WHERE %s";

    private final String table;
    private String filter;

    public DeleteStatement(String table) {
        this.table = table;
    }

    public DeleteStatement where(String filter) {
        this.filter = filter;
        return this;
    }

    public DeleteStatement where(Filter filter) {
        return where(filter.toString());
    }
    
    public DeleteStatement where(Function<Filter, String> filterBuilder) {
        return where(new Filter(filterBuilder).toString());
    }

    @Override
    public String build() {
        String statement = String.format(DELETE_FROM, table);
        if (filter != null) {
            statement += StringUtils.SPACE + String.format(WHERE, filter);
        }
        return statement;
    }
}
