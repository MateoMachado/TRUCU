package ucu.trucu.api;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity createAccount(@RequestBody Account newAccount) {
        try {
            accountDAO.insert(newAccount);
            LOGGER.info("Cuenta [CI=%s] creada correctamente", newAccount.getCI());
            return ResponseEntity.ok("Cuenta creada correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Imposible crear cuenta [CI=%s] -> %s", newAccount.getCI(), ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @GetMapping("/login")
    public ResponseEntity logIn(@RequestParam String email, @RequestParam String password) {
        Account account = accountDAO.findFirst(where -> where.eq("email", email));
        if (account != null) {
            // Sistema de autenticacion...
            if (password.equals(account.getPassword())) {
                return ResponseEntity.ok(account);
            } else {
                LOGGER.warn("Contraseña incorrecta de [email=%s]", email);
                return ResponseEntity.ok().body("Contraseña incorrecta");
            }
        } else {
            LOGGER.warn("Cuenta [email=%s] no existente", email);
            return ResponseEntity.ok().body("Cuenta no existente");
        }
    }

    @PostMapping("/update")
    public ResponseEntity updateAccount(String CI, Account newValues) {
        try {
            accountDAO.update(newValues, where -> where.eq("CI", CI));
            LOGGER.info("Valores actualizados en cuenta [CI=%s]", CI);
            return ResponseEntity.ok("Valores actualizados correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Imposible actualizar valores para cuenta [CI=%s] -> %s", CI, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAccount(@RequestParam String CI) {
        try {
            accountDAO.delete(where -> where.eq("CI", CI));
            LOGGER.info("Cuenta [CI=%s] eliminada correctamente", CI);
            return ResponseEntity.ok("Cuenta eliminada correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Imposible eliminar cuenta [CI=%s] -> %s", CI, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @GetMapping("/rol")
    public Rol getAccountRol(Account account) {
        return rolDAO.findByPK(account.getRolName());
    }
}
