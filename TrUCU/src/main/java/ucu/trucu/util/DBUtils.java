package ucu.trucu.util;

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
import ucu.trucu.database.EntityConversionException;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
public class DBUtils {

    private static final Logger LOGGER = LoggerFactory.create(DBUtils.class);

    /**
     * Convierte un java.sql.ResultSet en una lista de los respectivos objetos,
     * con valores mapeados. En caso de no machear una columna con una propiedad
     * del objeto, se setea por defecto en null. Para que machen, el objeto debe
     * tener un metodo publico setter con el formato:
     *
     * object.setColumnName(ColumnType value)
     *
     * @param <T>
     * @param resultSet
     * @param entityClass
     * @return
     * @throws EntityConversionException
     */
    public static <T> List<T> toEntityList(ResultSet resultSet, Class<T> entityClass) throws EntityConversionException {
        try {
            // Obtener setters disponibles en la entidad
            ColumnsMetaData metaData = getColumnsMetaData(entityClass, resultSet.getMetaData());
            Constructor<T> constructor = entityClass.getConstructor();
            List<T> entities = new LinkedList<>();

            while (resultSet.next()) {
                T newEntity = constructor.newInstance();
                entities.add(newEntity);
                // Ejecutar cada setter encontrado en la clase de la entidad
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

    public static <T> List<Map<String, Object>> objectsToPropertyMap(List<T> objects) {
        List<Map<String, Object>> mapList = new LinkedList<>();
        objects.forEach(object -> mapList.add(objectToPropertyMap(object)));
        return mapList;
    }

    public static <T> Map<String, Object> objectToPropertyMap(T object) {
        Map<String, Object> keyValue = new HashMap<>();
        for (Method method : object.getClass().getMethods()) {
            String methodName = method.getName();
            // Si el metodo es un getter lo ejecuto
            if (methodName.startsWith("get") && method.getParameterCount() == 0) {
                if (!methodName.equals("getClass")) {
                    try {
                        String propertyKey = methodName.substring("get".length());
                        Object propertyValue = method.invoke(object);
                        if (propertyValue != null) {
                            keyValue.put(propertyKey, propertyValue);
                        }
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        LOGGER.error(ex);
                    }
                }
            }
        }
        return keyValue;
    }

    public static List<Integer> getGeneratedId(ResultSet idResultSet) throws SQLException {
        List<Integer> list = new LinkedList<>();
        while (idResultSet.next()) {
            list.add(idResultSet.getInt(1));
        }
        return list;
    }

    /**
     * Obtiene informacion de las columnas de la tabla: Clase de la columna, y
     * metodo set asociado a cada una
     *
     * @param entityClass
     * @param metaData
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    private static ColumnsMetaData getColumnsMetaData(Class entityClass, ResultSetMetaData metaData) throws SQLException, ClassNotFoundException {
        ColumnsMetaData entityMetadata = new ColumnsMetaData();
        for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
            String columnName = metaData.getColumnName(columnIndex);
            if (!StringUtils.isEmpty(columnName)) {
                Class<?> columnClass = Class.forName(metaData.getColumnClassName(columnIndex));
                String setterName = "set" + StringUtils.capitalize(columnName);
                try {
                    Method setter = entityClass.getMethod(setterName, columnClass);
                    entityMetadata.addColumnSetter(columnName, setter);
                    entityMetadata.addColumnType(columnName, columnClass);
                } catch (NoSuchMethodException ex) {
                    LOGGER.warn("Property '%s' or method .%s(%s) not found in %s -> Setting default value null",
                            columnName, setterName, columnClass.getName(), entityClass);
                }
            }
        }
        return entityMetadata;
    }

    /**
     * Clase auxiliar para guardar nombre y tipo de las columnas
     */
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
