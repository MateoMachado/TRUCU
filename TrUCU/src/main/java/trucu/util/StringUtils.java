package trucu.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 *
 * @author NicoPuig
 */
public class StringUtils {

    public static final String COMA = ", ";
    public static final String COMA_NEW_LINE = ",\n";
    public static final String SPACE = " ";
    public static final String NEW_LINE = "\n";
    public static final String NEW_LINE_TABBED = "\n\t";
    public static final String UNDERSCORE = "_";

    public static <T> String join(String separator, T[] array) {
        return join(separator, array, String::valueOf);
    }

    public static <T> String join(String separator, Collection<T> collection) {
        return join(separator, collection, String::valueOf);
    }

    public static <T> String join(String separator, T[] array, Function<T, String> formatter) {
        String text = array.length == 0 ? "" : formatter.apply(array[0]);
        for (int i = 1; i < array.length; i++) {
            text += separator + formatter.apply(array[i]);
        }
        return text;
    }

    public static <T> String join(String separator, Collection<T> collection, Function<T, String> formatter) {
        Iterator<T> iterator = collection.iterator();
        String text = iterator.hasNext() ? formatter.apply(iterator.next()) : "";
        while (iterator.hasNext()) {
            text += separator + formatter.apply(iterator.next());
        }
        return text;
    }

    public static String getIf(boolean condition, String text) {
        return condition ? text : "";
    }
}
