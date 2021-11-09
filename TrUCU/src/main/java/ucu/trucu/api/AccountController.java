package ucu.trucu.api;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ucu.trucu.helper.AccountHelper;
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
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.create(AccountController.class);

    @Autowired
    private AccountHelper accountHelper;

    @PostMapping("/create")
    public ResponseEntity createAccount(@RequestBody Account newAccount) {
        try {
            accountHelper.createAccount(newAccount);
            LOGGER.info("Cuenta [Email=%s] creada correctamente", newAccount.getEmail());
            return ResponseEntity.ok("Cuenta creada correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Imposible crear cuenta [Email=%s] -> %s", newAccount.getEmail(), ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @GetMapping("/login")
    public ResponseEntity logIn(@RequestParam String email, @RequestParam String password) {
        Account account = accountHelper.getAccount(email);
        if (account != null) {
            if (accountHelper.logIn(account, password)) {
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
    public ResponseEntity updateAccount(@RequestParam String email, @RequestBody Account newValues) {
        try {
            accountHelper.updateAccountData(email, newValues);
            LOGGER.info("Valores actualizados en cuenta [Email=%s]", email);
            return ResponseEntity.ok("Valores actualizados correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Imposible actualizar valores para cuenta [Email=%s] -> %s", email, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAccount(@RequestParam String email, @RequestParam String password) {
        try {
            if (accountHelper.deleteAccount(email, password)) {
                LOGGER.info("Cuenta [Email=%s] eliminada correctamente", email);
                return ResponseEntity.ok("Cuenta eliminada correctamente");
            } else {
                LOGGER.warn("Contraseña incorrecta para cuenta [Email=%s]", email);
                return ResponseEntity.ok("Contraseña incorrecta");
            }
        } catch (SQLException ex) {
            LOGGER.error("Imposible eliminar cuenta [Email=%s] -> %s", email, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @GetMapping("/rol")
    public Rol getAccountRol(@RequestBody Account account) {
        return accountHelper.getAccountRol(account.getRolName());
    }
}
