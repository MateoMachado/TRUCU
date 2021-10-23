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

    private QueryBuilder() {

    }

    /*
    >>> 
        QueryBuilder.selectFrom("table") 
            .joinOn("table2", f -> f.eq("table1.id", "table2.id"))
            .joinOn("table3", f -> f.eq("table1.id", "table3.id"))
            .where(f -> f.eq("col1", 5))
            .orderAsc("col1", "col2");
    <<< 
        SELECT * FROM table
            INNER JOIN table2 ON table1.id = table2.id
            INNER JOIN table2 ON table1.id = table3.id
            WHERE col1 = 5
            ORDER BY col1, col2 ASC
     */
    public static SelectStatement selectFrom(String table) {
        return new SelectStatement(table);
    }

    /*
    >>> 
        QueryBuilder.selectFrom("table", "col1", "col2", "col3");
    <<< 
        SELECT col1, col2, col3 FROM table
     */
    public static SelectStatement selectFrom(String table, String... columns) {
        return new SelectStatement(table, columns);
    }

    /*
    >>>
        QueryBuilder.createTable("table")
            .addColumn("id", SQLType.BIGINT, 20)
            .addColumn("col1", SQLType.INT)
            .addColumn("col2", SQLType.DOUBLE, 24)
            .addColumn("table2Id", SQLType.INT, 20)
            .setCheck(f -> f.goet("col1", 50))
            .setDefaultValue("col2", 100.00)
            .setPrimaryKey("id")
            .setForeignKey("table2Id", "id", "table2", ON_DELETE_ACTION.CASCADE)
            .setNotNull("col2", true)
            .setUniqueKey("col1", "col2")
    <<< 
        CREATE TABLE table 
            (id BIGINT(20) NULL, col1 INT NULL, col2 DOUBLE(24) NOT NULL DEFAULT 100.0, table2Id INT(20) NULL,
            CHECK (col1 >= 50),
            CONSTRAINT PK_id PRIMARY KEY (id),
            CONSTRAINT UQ_col1_col2 UNIQUE (col1, col2),
            CONSTRAINT FK_table2Id FOREIGN KEY (table2Id) REFERENCES table2 (id) ON DELETE CASCADE)
    */
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
