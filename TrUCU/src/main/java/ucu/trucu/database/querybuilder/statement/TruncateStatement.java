package ucu.trucu.database.querybuilder.statement;

/**
 *
 * @author NicoPuig
 */
public class TruncateStatement implements Statement {

    private static final String TRUNCATE_TABLE = "TRUNCATE TABLE %s";

    private final String table;

    public TruncateStatement(String table) {
        this.table = table;
    }

    @Override
    public String build() {
        return String.format(TRUNCATE_TABLE, table);
    }
}
