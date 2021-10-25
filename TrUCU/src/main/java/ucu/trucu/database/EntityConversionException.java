package ucu.trucu.database;

/**
 *
 * @author NicoPuig
 */
public class EntityConversionException extends Exception {

    public EntityConversionException() {
        super();
    }

    public EntityConversionException(String message) {
        super(message);
    }

    public EntityConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
