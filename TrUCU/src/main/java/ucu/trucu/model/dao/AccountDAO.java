package ucu.trucu.model.dao;

import org.springframework.stereotype.Component;
import ucu.trucu.model.dto.Account;

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

    @Override
    public Account findByPK(Object... primaryKeys) {
        return findFirst(where -> where.eq("CI", primaryKeys[0]));
    }
}
