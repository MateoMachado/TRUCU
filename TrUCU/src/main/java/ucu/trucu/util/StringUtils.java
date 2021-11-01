package ucu.trucu.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 *
 * @author NicoPuig
 */
public class StringUtils {

    public static final String COMA = ", ";
    public static final String COMA_LN = ",\n";
    public static final String SPACE = " ";
    public static final String LN = "\n";
    public static final String LN_TABBED = "\n\t";
    public static final String UNDERSCORE = "_";

    public static String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }

    public static String decapitalize(String text) {
        return text.substring(0, 1).toLowerCase() + text.substring(1);
    }

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

    public static Concatenator concat(String separator, String originText) {
        return new Concatenator(separator, originText);
    }

    public static class Concatenator {

        private final String separator;
        private String text;

        private Concatenator(String separator, String originText) {
            this.separator = separator;
            this.text = originText;
        }

        public Concatenator preAdd(String text) {
            this.text = text + separator + this.text;
            return this;
        }

        public Concatenator preAddIf(boolean condition, String text) {
            return condition ? preAdd(text) : this;
        }

        public Concatenator add(String text) {
            this.text += separator + text;
            return this;
        }

        public Concatenator addIf(boolean condition, String text) {
            return condition ? add(text) : this;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
