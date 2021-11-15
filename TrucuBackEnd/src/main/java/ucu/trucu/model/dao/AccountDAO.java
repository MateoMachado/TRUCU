package ucu.trucu.model.dao;

import org.springframework.stereotype.Component;
import ucu.trucu.model.dto.Account;

/**
 *
 * @author NicoPuig
 */
@Component
public class AccountDAO extends AbstractDAO<Account> {

    public static final String ACCOUNT = "Account";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";

    @Override
    public String getTable() {
        return ACCOUNT;
    }

    @Override
    public Class<Account> getEntityClass() {
        return Account.class;
    }

    @Override
    public Account findByPK(Object... primaryKeys) {
        return findFirst(where -> where.eq(EMAIL, primaryKeys[0]));
    }
}
