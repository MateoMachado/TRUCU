package trucu.database.querybuilder;

import trucu.database.querybuilder.statement.AlterTableStatement;
import trucu.database.querybuilder.statement.CreateTableStatement;
import trucu.database.querybuilder.statement.DeleteStatement;
import trucu.database.querybuilder.statement.DropStatement;
import trucu.database.querybuilder.statement.InsertStatement;
import trucu.database.querybuilder.statement.SelectStatement;
import trucu.database.querybuilder.statement.TruncateStatement;
import trucu.database.querybuilder.statement.UpdateStatement;

/**
 *
 * @author NicoPuig
 */
public class QueryBuilder {

    // OPERATIONS
    private final static String DROP_TABLE = "DROP TABLE %s";
    private final static String TRUNCATE_TABLE = "TRUNCATE TABLE %s";
    private final static String DELETE_FROM = "DELETE FROM %s";

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

    public static UpdateStatement update(String table) {
        return new UpdateStatement(table);
    }

    public static DeleteStatement deleteFrom(String table) {
        return new DeleteStatement(table);
    }

    public static DropStatement dropTable(String table) {
        return new DropStatement(table);
    }

    public static TruncateStatement truncateTable(String table) {
        return new TruncateStatement(table);
    }
}
