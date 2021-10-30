package ucu.trucu.helper;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.model.dao.AccountDAO;
import ucu.trucu.model.dao.RolDAO;
import ucu.trucu.model.dto.Account;
import ucu.trucu.model.dto.Rol;

/**
 *
 * @author NicoPuig
 */
@Service
public class AccountHelper {

    private static final String CI = "CI";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private RolDAO rolDAO;

    public void createAccount(Account newAccount) throws SQLException {
        accountDAO.insert(newAccount);
    }

    public Account getAccount(String email) {
        return accountDAO.findFirst(where -> where.eq(EMAIL, email));
    }

    public boolean logIn(Account account, String password) {
        // Sistema de autenticacion...
        return password.equals(account.getPassword());
    }

    public void updateAccountData(String ci, Account newValues) throws SQLException {
        accountDAO.update(newValues, where -> where.eq(CI, ci));
    }

    public boolean deleteAccount(String ci, String password) throws SQLException {
        int deletedRows = accountDAO.delete(where -> where.and(
                where.eq(CI, ci),
                where.eq(PASSWORD, password))
        );
        return deletedRows == 1;
    }

    public Rol getAccountRol(String accountRolName) {
        return rolDAO.findByPK(accountRolName);
    }
}
