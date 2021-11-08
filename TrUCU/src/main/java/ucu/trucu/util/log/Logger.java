package ucu.trucu.util.log;

/**
 *
 * @author NicoPuig
 */
public class Logger {

    private final static String DEFAULT_FORMAT = "%s\t[%s]\t%s";

    private final Class loggedClass;
    private final Log[] classLoggers;

    private enum MessageType {
        INFO, WARN, ERROR, QUERY;
    }

    public Logger(Class loggedClass, Log[] logs) {
        this.loggedClass = loggedClass;
        this.classLoggers = logs;
    }

    private void log(Object message, MessageType type) {
        for (Log logger : classLoggers) {
            logger.log(toLogFormat(type, message));
        }
    }

    public void info(Object message) {
        log(message, MessageType.INFO);
    }

    public void info(String messagePattern, Object... args) {
        info(String.format(messagePattern, args));
    }

    public void warn(Object message) {
        log(message, MessageType.WARN);
    }

    public void warn(String messagePattern, Object... args) {
        warn(String.format(messagePattern, args));
    }

    public void error(Object message) {
        log(message, MessageType.ERROR);
    }

    public void error(String messagePattern, Object... args) {
        error(String.format(messagePattern, args));
    }

    public void query(String query) {
        log(String.format(">>> %s;", query), MessageType.QUERY);
    }

    private String toLogFormat(MessageType type, Object message) {
        return String.format(DEFAULT_FORMAT, type, loggedClass.getName(), message);
    }
}
