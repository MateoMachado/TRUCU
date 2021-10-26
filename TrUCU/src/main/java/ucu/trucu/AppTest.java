package ucu.trucu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.model.dao.AccountDAO;
import ucu.trucu.model.dao.ReasonDAO;
import ucu.trucu.model.dto.Account;
import ucu.trucu.model.dto.Reason;

/**
 *
 * @author NicoPuig
 */
@Component
public class AppTest {

    @Autowired
    private ReasonDAO dao;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        List select = dao.select(QueryBuilder.selectFrom(dao.getTable()));
        int i = 1;
        for (Object obj : select) {
            System.out.println("Object" + i++);
            for (Method method : dao.getEntityClass().getMethods()) {
                if (method.getName().startsWith("get")) {
                    try {
                        System.out.println("\t" + method.getName() + ": " + method.invoke(obj));
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
    }
}
