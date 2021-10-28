package ucu.trucu.model.service;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
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
@RestController
@RequestMapping("trucu/account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.create(AccountController.class);

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private RolDAO rolDAO;

    @PostMapping("/create")
    public void createAccount(@RequestBody Account newAccount) {
        try {
            accountDAO.insert(newAccount);
            LOGGER.info("Cuenta [CI=%s] creada correctamente", newAccount.getCI());
        } catch (SQLException ex) {
            LOGGER.error("Imposible crear cuenta [CI=%s] -> %s", newAccount.getCI(), ex.getMessage());
        }
    }

    @GetMapping("/login")
    public Account logIn(@RequestParam String email, @RequestParam String password) {
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

    @PostMapping("/update")
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

    @DeleteMapping("/delete")
    public boolean deleteAccount(@RequestParam String CI) {
        try {
            accountDAO.delete(where -> where.eq("CI", CI));
            LOGGER.info("Cuenta [CI=%s] eliminada correctamente", CI);
            return true;
        } catch (SQLException ex) {
            LOGGER.error("Imposible borrar cuenta [CI=%s] -> %s", CI, ex);
            return false;
        }
    }

    @GetMapping("/rol")
    public Rol getAccountRol(Account account) {
        return rolDAO.findByPK(account.getRolName());
    }
}
