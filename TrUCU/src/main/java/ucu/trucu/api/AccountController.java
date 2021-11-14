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
import ucu.trucu.database.DBController;
import ucu.trucu.helper.AccountHelper;
import ucu.trucu.model.dto.Account;
import ucu.trucu.model.dto.Rol;
import ucu.trucu.util.api.Message;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@RestController
@RequestMapping("trucu/account")
@CrossOrigin(origins = {"${trucu.front.url.local}", "${trucu.front.url.firebase}"})
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.create(AccountController.class);

    @Autowired
    private AccountHelper accountHelper;

    @Autowired
    private DBController dbController;

    @PostMapping("/create")
    public ResponseEntity createAccount(@RequestBody Account newAccount) {
        try {
            accountHelper.create(newAccount);
            dbController.commit();
            LOGGER.info("Cuenta [Email=%s] creada correctamente", newAccount.getEmail());
            return ResponseEntity.ok(new Message("Cuenta creada correctamente"));
        } catch (SQLException ex) {
            LOGGER.error("Imposible crear cuenta [Email=%s] -> %s", newAccount.getEmail(), ex.getMessage());
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
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
                return ResponseEntity.ok().body(new Message("Contraseña incorrecta"));
            }
        } else {
            LOGGER.warn("Cuenta [email=%s] no existente", email);
            return ResponseEntity.ok().body(new Message("Cuenta no existente"));
        }
    }

    @PostMapping("/update")
    public ResponseEntity updateAccount(@RequestParam String email, @RequestBody Account newValues) {
        try {
            accountHelper.update(email, newValues);
            dbController.commit();
            LOGGER.info("Valores actualizados en cuenta [Email=%s]", email);
            return ResponseEntity.ok(new Message("Valores actualizados correctamente"));
        } catch (SQLException ex) {
            LOGGER.error("Imposible actualizar valores para cuenta [Email=%s] -> %s", email, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAccount(@RequestParam String email) {
        try {
            if (accountHelper.delete(email)) {
                dbController.commit();
                LOGGER.info("Cuenta [Email=%s] eliminada correctamente", email);
                return ResponseEntity.ok(new Message("Cuenta eliminada correctamente"));
            } else {
                LOGGER.warn("Cuenta no existente [Email=%s]", email);
                return ResponseEntity.ok(new Message("Cuenta no existente"));
            }
        } catch (SQLException ex) {
            LOGGER.error("Imposible eliminar cuenta [Email=%s] -> %s", email, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @GetMapping("/get")
    public ResponseEntity<Account> getAccount(@RequestParam String email) {
        return ResponseEntity.ok(accountHelper.getAccount(email));
    }

    @GetMapping("/rol")
    public Rol getAccountRol(@RequestBody Account account) {
        return accountHelper.getAccountRol(account.getRolName());
    }
}
