package trucu.database.querybuilder.statement;

/**
 *
 * @author NicoPuig
 */
public class DropStatement implements Statement {

    private final String table;

    public DropStatement(String table) {
        this.table = table;
    }

    @Override
    public String build() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
