package trucu;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import trucu.database.querybuilder.QueryBuilder;
import trucu.model.dao.AccountDAO;
import trucu.model.dto.Account;

/**
 *
 * @author NicoPuig
 */
@Component
public class AppTest {

    @Autowired
    private AccountDAO dao;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        List<Account> select = dao.select(QueryBuilder.selectFrom(dao.getTable()));
    }
}
