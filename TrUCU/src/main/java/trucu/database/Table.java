package trucu.database;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Consumer;

/**
 *
 * @author NicoPuig
 */
public class Table {

    private final ArrayList<ArrayList<Object>> data;
    private final ArrayList<String> columnNames;
    private final ArrayList<SQLType> columnTypes;
    private int columnCount;
    private int rowCount;

    public Table() {
        this.data = new ArrayList<>();
        this.columnNames = new ArrayList<>();
        this.columnTypes = new ArrayList<>();
        this.columnCount = 0;
        this.rowCount = 0;
    }

    public Table(ArrayList<ArrayList<Object>> data, ArrayList<String> columnNames, ArrayList<SQLType> columnTypes) {
        this.data = data;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
        this.columnCount = columnNames.size();
        this.rowCount = data.size();
    }

    public ArrayList<Object> getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    public <T> ArrayList<T> getColumn(int index) {
        Class<T> columnType = getColumnType(index);
        ArrayList<T> column = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            column.add(columnType.cast(getValueAt(rowIndex, index)));
        }
        return column;
    }

    public <T> T getValueAt(int row, int column, Class<T> clazz) {
        return clazz.cast(data.get(row).get(column));
    }

    public Object getValueAt(int row, int column) {
        return data.get(row).get(column);
    }

    public void setValueAt(Object object, int row, int column) throws ClassNotFoundException {
        data.get(row).set(column, columnTypes.get(column).getJavaType().cast(object));
    }

    public void foreachRow(Consumer<ArrayList<Object>> consumer) {
        data.forEach(consumer::accept);
    }

    public String[] getColumnNames() {
        return columnNames.toArray(new String[0]);
    }

    public SQLType[] getAllColumnsSQLTypes() {
        return columnTypes.toArray(new SQLType[0]);
    }

    public SQLType getColumnSQLType(int index) {
        return columnTypes.get(index);
    }

    public Class getColumnType(int index) {
        return columnTypes.get(index).getJavaType();
    }

    public boolean isEmpty() {
        return data.isEmpty();
    }

    public int getColumnCount() {
        return columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public String getColumnName(int index) {
        return columnNames.get(index);
    }

    public void addRow(ArrayList<Object> row) {
        data.add(row);
        rowCount++;
    }

    public int getColumnIndex(String name) {
        return columnNames.indexOf(name);
    }

    public <T> void addColumn(String name, Class<T> type, ArrayList<T> column) {
        int columnIndex = 0;
        for (ArrayList<Object> row : data) {
            try {
                row.add(column.get(columnIndex++));
            } catch (IndexOutOfBoundsException e) {
                row.add(null);
            }
        }
        columnNames.add(name);
        columnTypes.add(SQLType.toSQLType(type));
        columnCount++;
    }

    public static Table toTable(ResultSet resultSet) throws SQLException {
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();
        ArrayList<String> columnNames = new ArrayList<>(columnCount);
        ArrayList<SQLType> columnTypes = new ArrayList<>(columnCount);
        ArrayList<ArrayList<Object>> rows = new ArrayList<>();
        boolean isFirstRow = true;
        while (resultSet.next()) {
            ArrayList<Object> row = new ArrayList<>(columnCount);
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                row.add(columnIndex, resultSet.getObject(columnIndex + 1));
                if (isFirstRow) {
                    columnNames.add(columnIndex, metaData.getColumnLabel(columnIndex + 1));
                    columnTypes.add(columnIndex, SQLType.toSQLType(metaData.getColumnTypeName(columnIndex + 1)));
                }
            }
            isFirstRow = false;
            rows.add(row);
        }
        return new Table(rows, columnNames, columnTypes);
    }
}
