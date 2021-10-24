package trucu.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import trucu.database.EntityConversionException;

/**
 *
 * @author NicoPuig
 */
public class DBUtils {

    public static <T> List<T> toEntityList(ResultSet resultSet, Class<T> entityClass) throws EntityConversionException {
        try {
            ColumnsMetaData metaData = getColumnsMetaData(entityClass, resultSet.getMetaData());
            Constructor<T> constructor = entityClass.getDeclaredConstructor();
            List<T> entities = new LinkedList<>();

            while (resultSet.next()) {
                T newEntity = constructor.newInstance();
                entities.add(newEntity);
                for (Map.Entry<String, Method> entry : metaData.columnSetters.entrySet()) {
                    String column = entry.getKey();
                    entry.getValue().invoke(newEntity, resultSet.getObject(column, metaData.columnTypes.get(column)));
                }
            }
            return entities;
        } catch (SQLException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | InstantiationException | ClassNotFoundException | NoSuchMethodException ex) {
            throw new EntityConversionException(String.format("%s - %s", ex.getClass(), ex.getMessage()), ex);
        }
    }

    private static ColumnsMetaData getColumnsMetaData(Class entityClass, ResultSetMetaData metaData) throws SQLException, ClassNotFoundException, NoSuchMethodException {
        ColumnsMetaData entityMetadata = new ColumnsMetaData();
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
            Class<?> columnClass = Class.forName(metaData.getColumnClassName(columnIndex));
            String columnName = metaData.getColumnName(columnIndex);
            String setterName = "set" + StringUtils.capitalize(columnName);
            Method setter = entityClass.getMethod(setterName, columnClass);
            entityMetadata.addColumnSetter(columnName, setter);
            entityMetadata.addColumnType(columnName, columnClass);
        }
        return entityMetadata;
    }

    private static class ColumnsMetaData {

        private final Map<String, Class<?>> columnTypes = new HashMap<>();
        private final Map<String, Method> columnSetters = new HashMap<>();

        public void addColumnType(String columnName, Class<?> columnType) {
            columnTypes.put(columnName, columnType);
        }

        public void addColumnSetter(String columnName, Method setter) {
            columnSetters.put(columnName, setter);
        }
    }
}
