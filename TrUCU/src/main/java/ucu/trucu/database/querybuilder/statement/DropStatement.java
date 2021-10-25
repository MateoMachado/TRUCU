package ucu.trucu.database.querybuilder.statement;

/**
 *
 * @author NicoPuig
 */
public class DropStatement implements Statement {

    private static final String DROP_TABLE = "DROP TABLE %s";

    private final String table;

    public DropStatement(String table) {
        this.table = table;
    }

    @Override
    public String build() {
        return String.format(DROP_TABLE, table);
    }
}
