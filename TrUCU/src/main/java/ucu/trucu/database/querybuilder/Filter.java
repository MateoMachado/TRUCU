package ucu.trucu.database.querybuilder;

import java.util.Collection;
import java.util.function.Function;
import ucu.trucu.database.querybuilder.statement.SelectStatement;
import ucu.trucu.util.StringUtils;

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
        return joinOperand("=", key, String.format("'%s'", value));
    }

    public <T> String lt(String key, T value) {
        return joinOperand("<", key, String.format("'%s'", value));
    }

    public <T> String gt(String key, T value) {
        return joinOperand(">", key, String.format("'%s'", value));
    }

    public <T> String loet(String key, T value) {
        return joinOperand("<=", key, String.format("'%s'", value));
    }

    public <T> String goet(String key, T value) {
        return joinOperand(">=", key, String.format("'%s'", value));
    }

    public <T> String notEq(String key, T value) {
        return joinOperand("!=", key, String.format("'%s'", value));
    }

    public <T> String like(String key, T value) {
        return joinOperand("LIKE", key, String.format("'%s'", value));
    }

    public String in(String key, SelectStatement selectStatement) {
        return joinOperand("IN", key, String.format("(%s)", selectStatement.build()));
    }

    public <T> String in(String key, Collection<T> list) {
        return joinOperand("IN", key, String.format("(%s)", StringUtils.join(StringUtils.COMA, list, value -> String.format("'%s'", value))));
    }

    public <T> String in(String key, T... list) {
        return joinOperand("IN", key, String.format("(%s)", StringUtils.join(StringUtils.COMA, list, value -> String.format("'%s'", value))));
    }

    public String not(String operation) {
        return "NOT " + operation;
    }

    public String isNotNull(String key) {
        return key + " IS NOT NULL";
    }

    public String exists(SelectStatement select) {
        return String.format("EXISTS (%s)", select.build());
    }

    public String and(String... conditions) {
        return StringUtils.join(" AND ", conditions, condition -> String.format("(%s)", condition));
    }

    public String or(String... conditions) {
        return StringUtils.join(" OR ", conditions, condition -> String.format("(%s)", condition));
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
