package ucu.trucu.database.querybuilder;

import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;
import ucu.trucu.util.StringUtils;

/**
 *
 * @author NicoPuig
 */
public class CaseExpression {

    private static final String CASE = "CASE %s END";
    private static final String WHEN = "WHEN '%s' THEN '%s'";
    private static final String ELSE = "ELSE '%s'";
    private final List<Case> caseList = new LinkedList<>();

    private String param;
    private Object elseCase;

    public CaseExpression forParam(String param) {
        this.param = param;
        return this;
    }

    public CaseExpression addCase(Function<Filter, String> condition, Object result) {
        this.caseList.add(new Case(new Filter(condition), result));
        return this;
    }

    public CaseExpression addCase(Object value, Object result) {
        this.caseList.add(new Case(value, result));
        return this;
    }

    public CaseExpression orElse(Object elseReturn) {
        this.elseCase = elseReturn;
        return this;
    }

    public String build() {
        if (!caseList.isEmpty()) {
            String cases = StringUtils.join(StringUtils.SPACE, caseList, aCase -> String.format(WHEN, aCase.value, aCase.result));
            if (param != null) {
                cases = param + StringUtils.SPACE + cases;
            }
            if (elseCase != null) {
                cases += StringUtils.SPACE + String.format(ELSE, elseCase);
            }
            return String.format(CASE, cases);
        }
        return null;
    }

    private class Case {

        private Object value;
        private final Object result;

        public Case(Object value, Object result) {
            this.value = value;
            this.result = result;
        }
    }
}
