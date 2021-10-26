package ucu.trucu.database;

import ucu.trucu.database.querybuilder.SQLType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import ucu.trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class Table {

    private final ArrayList<ArrayList<Object>> data;
    private final ArrayList<String> columnNames;
    private final ArrayList<SQLType> columnTypes;

    public Table() {
        this.data = new ArrayList<>();
        this.columnNames = new ArrayList<>();
        this.columnTypes = new ArrayList<>();
    }

    public Table(ArrayList<ArrayList<Object>> data, ArrayList<String> columnNames, ArrayList<SQLType> columnTypes) {
        this.data = data;
        this.columnNames = columnNames;
        this.columnTypes = columnTypes;
    }

    public ArrayList<Object> getRow(int rowIndex) {
        return data.get(rowIndex);
    }

    public <T> ArrayList<T> getColumn(int index) {
        Class<T> columnType = getColumnType(index);
        ArrayList<T> column = new ArrayList<>();
        for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
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
        return columnNames.size();
    }

    public int getRowCount() {
        return data.size();
    }

    public String getColumnName(int index) {
        return columnNames.get(index);
    }

    public void addRow(ArrayList<Object> row) {
        data.add(row);
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
    }

    /**
     * Convierte la tabla en una lista de entidades
     *
     * @param <T>
     * @param entityClass
     * @return
     * @throws ucu.trucu.database.EntityConversionException
     * @throws IllegalArgumentException
     */
    public <T> List<T> toEntityList(Class<T> entityClass) throws EntityConversionException {
        List<T> entities = new LinkedList<>();
        try {
            Constructor<T> constructor = entityClass.getDeclaredConstructor();
            for (int rowIndex = 0; rowIndex < getRowCount(); rowIndex++) {
                T newEntity = constructor.newInstance();
                for (int columnIndex = 0; columnIndex < getColumnCount(); columnIndex++) {
                    Class columnJavaType = columnTypes.get(columnIndex).getJavaType();
                    String setterName = "set" + StringUtils.capitalize(getColumnName(columnIndex));
                    Method setter = entityClass.getMethod(setterName, columnJavaType);
                    setter.invoke(newEntity, getValueAt(rowIndex, columnIndex, columnJavaType));
                }
                entities.add(newEntity);
            }
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException ex) {
            throw new EntityConversionException(ex.getMessage(), ex);
        }
        return entities;
    }

    /**
     * Convierte un ResultSet a un objeto Tabla mas facil de utilizar
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
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
