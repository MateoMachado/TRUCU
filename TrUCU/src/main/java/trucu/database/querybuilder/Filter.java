package trucu.database.querybuilder;

import java.util.Collection;
import java.util.function.Function;
import trucu.database.querybuilder.statement.SelectStatement;
import trucu.util.StringUtils;

/**
 * Filter for querys
 *
 * @author NicoPuig
 */
public class Filter {

    private final String filter;

    public Filter(String filter) {
        this.filter = filter;
    }

    public Filter(Function<Filter, String> builder) {
        this.filter = builder.apply(this);
    }

    public <T> String eq(String key, T value) {
        return joinOperand("=", key, value);
    }

    public <T> String lt(String key, T value) {
        return joinOperand("<", key, value);
    }

    public <T> String gt(String key, T value) {
        return joinOperand(">", key, value);
    }

    public <T> String loet(String key, T value) {
        return joinOperand("<=", key, value);
    }

    public <T> String goet(String key, T value) {
        return joinOperand(">=", key, value);
    }

    public <T> String notEq(String key, T value) {
        return joinOperand("<>", key, value);
    }

    public <T> String like(String key, T value) {
        return joinOperand("LIKE", key, String.format("'%%%s%%'", value));
    }

    public <T> String in(String key, Collection<T> list) {
        return joinOperand("IN", key, String.format("(%s)", StringUtils.join(StringUtils.COMA, list)));
    }

    public <T> String in(String key, T... list) {
        return joinOperand("IN", key, String.format("(%s)", StringUtils.join(StringUtils.COMA, list)));
    }

    public String isNotNull(String key) {
        return key + " IS NOT NULL";
    }

    public String exists(SelectStatement select) {
        return String.format("EXISTS (%s)", select.build());
    }

    public String and(String condition1, String condition2, String... otherConditions) {
        return joinConjunction("AND", condition1, condition2, otherConditions);
    }

    public String or(String condition1, String condition2, String... otherConditions) {
        return joinConjunction("OR", condition1, condition2, otherConditions);
    }

    private static String joinConjunction(String operand, String condition1, String condition2, String[] otherConditions) {
        if (condition1 == null || condition2 == null) {
            throw new IllegalArgumentException("Conditions cant be null");
        }
        String result = String.format("(%s) %s (%s)", condition1, operand, condition2);
        for (String condition : otherConditions) {
            result += String.format(" %s (%s)", operand, condition);
        }
        return result;
    }

    private static <T> String joinOperand(String operand, String key, T value) {
        if (key == null) {
            throw new IllegalArgumentException("Column key cant be null");
        }
        return String.format("%s %s %s", key, operand, value);
    }

    public static Filter build(Function<Filter, String> builder) {
        return new Filter(builder);
    }

    @Override
    public String toString() {
        return filter;
    }
}
