package ucu.trucu.database.querybuilder;

import ucu.trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class ForeignTableKey {

    private static final String REFERENCE = "CONSTRAINT FK_%s FOREIGN KEY (%s) REFERENCES %s (%s)";
    private static final String ON_DELETE = "ON DELETE %s";

    public enum ON_DELETE_ACTION {
        CASCADE("CASCADE"), SET_NULL("SET NULL");

        private final String syntax;

        private ON_DELETE_ACTION(String syntax) {
            this.syntax = syntax;
        }

        public String getSyntax() {
            return syntax;
        }
    }

    private ON_DELETE_ACTION deleteAction;
    private final TableColumn[] columns;
    private final String referencedTable;
    private final String[] columnReferences;

    public ForeignTableKey(TableColumn[] columns, String[] columnReferences, String referencedTable) {
        this.columns = columns;
        this.referencedTable = referencedTable;
        this.columnReferences = columnReferences;
    }

    public ForeignTableKey(TableColumn[] columns, String[] columnReferences, String referencedTable, ON_DELETE_ACTION deleteAction) {
        this.columns = columns;
        this.referencedTable = referencedTable;
        this.columnReferences = columnReferences;
        this.deleteAction = deleteAction;
    }

    public String toCreateTableConstraintFormat() {
        String contraintName = StringUtils.join(StringUtils.UNDERSCORE, columns, TableColumn::getName);
        String keys = StringUtils.join(StringUtils.COMA, columns, TableColumn::getName);
        String references = StringUtils.join(StringUtils.COMA, columnReferences);
        String statement = String.format(REFERENCE, contraintName, keys, referencedTable, references);
        if (deleteAction != null) {
            statement += StringUtils.SPACE + String.format(ON_DELETE, deleteAction.getSyntax());
        }
        return statement;
    }
}
