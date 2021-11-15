package ucu.trucu.util.log;

/**
 *
 * @author NicoPuig
 */
public class ConsoleLog implements Log {

    @Override
    public void log(String message) {
        System.out.println(message);
    }
}
