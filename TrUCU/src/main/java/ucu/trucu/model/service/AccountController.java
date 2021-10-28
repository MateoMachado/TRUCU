package ucu.trucu.model.service;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ucu.trucu.model.dao.AccountDAO;
import ucu.trucu.model.dao.RolDAO;
import ucu.trucu.model.dto.Account;
import ucu.trucu.model.dto.Rol;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@Controller
public class AccountController {
    
    private static final Logger LOGGER = LoggerFactory.create(AccountController.class);
    
    @Autowired
    private AccountDAO accountDAO;
    
    @Autowired
    private RolDAO rolDAO;
    
    public void createAccount(Account newAccount) {
        try {
            accountDAO.insert(newAccount);
            LOGGER.info("Cuenta [CI=%s] creada correctamente", newAccount.getCI());
        } catch (SQLException ex) {
            LOGGER.error("Imposible crear cuenta [CI=%s] -> %s", newAccount.getCI(), ex.getMessage());
        }
    }
    
    public Account logIn(String email, String password) {
        Account account = accountDAO.findFirst(where -> where.eq("email", email));
        if (account != null) {
            // Sistema de autenticacion...
            if (password.equals(account.getPassword())) {
                return account;
            }
        } else {
            LOGGER.warn("Cuenta [email=%s] no existente", email);
        }
        return null;
    }
    
    public boolean updateAccount(String CI, Account newValues) {
        try {
            accountDAO.update(newValues, where -> where.eq("CI", CI));
            LOGGER.info("Valores actualizados en cuenta [CI=%s]", CI);
            return true;
        } catch (SQLException ex) {
            LOGGER.error("Imposible actualizar valores para cuenta [CI=%s] -> %s", CI, ex);
            return false;
        }
    }
    
    public boolean deleteAccount(String CI) {
        try {
            accountDAO.delete(where -> where.eq("CI", CI));
            LOGGER.info("Cuenta [CI=%s] eliminada correctamente", CI);
            return true;
        } catch (SQLException ex) {
            LOGGER.error("Imposible borrar cuenta [CI=%s] -> %s", CI, ex);
            return false;
        }
    }
    
    public Rol getAccountRol(Account account) {
        return rolDAO.findByPK(account.getRolName());
    }
}
