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

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private RolDAO rolDAO;

    public String create(Account newAccount) throws SQLException {
        accountDAO.insert(newAccount);
        return newAccount.getEmail();
    }

    public int update(String email, Account newValues) throws SQLException {
        return accountDAO.update(newValues, where -> where.eq(AccountDAO.EMAIL, email));
    }

    public boolean delete(String email) throws SQLException {
        return accountDAO.delete(where -> where.eq(AccountDAO.EMAIL, email)) == 1;
    }

    public Account getAccount(String email) {
        return accountDAO.findByPK(email);
    }

    public boolean logIn(Account account, String password) {
        // Sistema de autenticacion...
        return password.equals(account.getPassword());
    }

    public Rol getAccountRol(String accountRolName) {
        return rolDAO.findByPK(accountRolName);
    }
}
