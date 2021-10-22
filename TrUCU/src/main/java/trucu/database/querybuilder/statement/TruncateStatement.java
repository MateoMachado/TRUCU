package trucu.database.querybuilder.statement;

/**
 *
 * @author NicoPuig
 */
public class TruncateStatement implements Statement {

    private final String table;

    public TruncateStatement(String table) {
        this.table = table;
    }

    @Override
    public String build() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
