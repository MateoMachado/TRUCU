package ucu.trucu.database.querybuilder;

import ucu.trucu.database.querybuilder.statement.DeleteStatement;
import ucu.trucu.database.querybuilder.statement.InsertStatement;
import ucu.trucu.database.querybuilder.statement.SelectStatement;
import ucu.trucu.database.querybuilder.statement.UpdateStatement;

/**
 *
 * @author NicoPuig
 */
public class QueryBuilder {

    private QueryBuilder() {

    }

    public static SelectStatement selectFrom(String table, String... columns) {
        return new SelectStatement(table, columns);
    }

    public static SelectStatement selectDistinctFrom(String table, String... columns) {
        return new SelectStatement(table, columns, true);
    }

    public static UpdateStatement update(String table) {
        return new UpdateStatement(table);
    }

    public static DeleteStatement deleteFrom(String table) {
        return new DeleteStatement(table);
    }
    
    public static InsertStatement insertInto(String table) {
        return new InsertStatement(table);
    }
    
    public static SelectStatement countFrom(String table){
        return new SelectStatement(table, "COUNT(*) AS 'count'");
    }
}
