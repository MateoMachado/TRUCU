package trucu.database;

import java.util.Date;
import java.util.function.Predicate;

/**
 *
 * @author NicoPuig
 */
public enum SQLType {

    VARCHAR(String.class),
    INT(Integer.class),
    BIGINT(Long.class),
    BOOLEAN(Boolean.class),
    DOUBLE(Double.class),
    DATETIME(Date.class),
    FLOAT(Float.class);

    private final Class javaType;

    private SQLType(Class javaType) {
        this.javaType = javaType;
    }

    public Class getJavaType() {
        return javaType;
    }

    public static Class toJavaType(String type) {
        return toSQLType(type).javaType;
    }

    public static SQLType toSQLType(Class javaType) {
        String error = String.format("Clase {%s} no asociada a ningun tipo SQL", javaType);
        return findOrThrow(t -> t.javaType.equals(javaType), error);
    }

    public static SQLType toSQLType(String type) {
        String error = String.format("Tipo SQL {%s} no encontrado", type);
        return findOrThrow(t -> t.name().equalsIgnoreCase(type), error);
    }

    private static SQLType findOrThrow(Predicate<SQLType> filter, String message) {
        for (SQLType type : values()) {
            if (filter.test(type)) {
                return type;
            }
        }
        throw new IllegalArgumentException(message);
    }
}
