package ucu.trucu.util.log;

import java.util.function.Supplier;

/**
 *
 * @author NicoPuig
 */
public class LoggerFactory {

    private static Supplier<Log>[] defaultLogs;

    public static void setProgramLogs(Supplier<Log>... newProgramLogs) {
        defaultLogs = newProgramLogs;
    }

    public static Logger create(Class loggedClass) {
        return create(loggedClass, defaultLogs);
    }

    public static Logger create(Class loggedClass, Supplier<Log>... programLogs) {
        Log[] logs = new Log[programLogs.length];
        for (int i = 0; i < programLogs.length; i++) {
            logs[i] = programLogs[i].get();
        }
        return new Logger(loggedClass, logs);
    }
}
