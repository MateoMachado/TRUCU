package trucu.database.querybuilder;

import trucu.database.querybuilder.statement.AlterTableStatement;
import trucu.database.querybuilder.statement.CreateTableStatement;
import trucu.database.querybuilder.statement.InsertStatement;
import trucu.database.querybuilder.statement.SelectStatement;

/**
 *
 * @author NicoPuig
 */
public class QueryBuilder {

    // OPERATIONS
    private final static String DROP_TABLE = "DROP TABLE %s";
    private final static String TRUNCATE_TABLE = "TRUNCATE TABLE %s";

    private final static String DELETE_FROM = "DELETE FROM %s";
    private final static String UPDATE = "UPDATE %s SET %s";

    protected QueryBuilder() {

    }

    public static SelectStatement selectFrom(String table) {
        return new SelectStatement(table);
    }

    public static SelectStatement selectFrom(String table, String... columns) {
        return new SelectStatement(table, columns);
    }

    public static CreateTableStatement createTable(String tableName) {
        return new CreateTableStatement(tableName);
    }

    public static InsertStatement insertInto(String table) {
        return new InsertStatement(table);
    }

    public static AlterTableStatement alterTable(String table) {
        return new AlterTableStatement(table);
    }
}
