package trucu.model.dao;

import org.springframework.stereotype.Component;
import trucu.model.dto.Account;

/**
 *
 * @author NicoPuig
 */
@Component
public class AccountDAO extends AbstractDAO<Account> {

    @Override
    public String getTable() {
        return "Account";
    }

    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }
}
