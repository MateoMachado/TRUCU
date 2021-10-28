package ucu.trucu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ucu.trucu.database.DBController;
import ucu.trucu.model.dao.AbstractDAO;
import ucu.trucu.model.dao.AccountDAO;
import ucu.trucu.model.dao.ImageDAO;
import ucu.trucu.model.dao.PublicationDAO;
import ucu.trucu.model.dao.ReasonDAO;
import ucu.trucu.model.dao.ReportDAO;
import ucu.trucu.model.dao.RolDAO;
import ucu.trucu.model.dto.Account;
import ucu.trucu.model.dto.Rol;
import ucu.trucu.model.service.AccountController;

/**
 *
 * CORRER CUALQUIER CODIGO DE PRUEBA ACA
 *
 * Esta clase no es parte de la app, solo para probar cosas en la aplicacion ya
 * corriendo (dependencias inyectadas, bd conectada, etc). La anotacion
 * EventListener(ApplicationReadyEvent.class) especifica que el metodo tiene que
 * correr despues de levantar la app.
 *
 * Despues probablemente se pueda probar desde una API REST
 *
 * @author NicoPuig
 */
//@Component
public class AppTest {

    @Autowired
    private AccountDAO accountDAO;

    @Autowired
    private AccountController accountController;

//    @EventListener(ApplicationReadyEvent.class)
    public void start() {

        Account logIn = accountController.logIn("nicopuig@mail.com", "1234567");
        Rol rol = accountController.getAccountRol(logIn);
        
        
        System.out.println(rol);
//        Account account = new Account();
//        account.setName("Esteban");
//        account.setLastName("Barrios");
//        account.setPassword("6456");
//        account.setCI("7");
//        account.setBirthDate(Date.valueOf("1942-02-14"));
//        account.setRolName("User");
//        account.setEmail("ebarrios@ucu.edu.uy");
//
//        accountController.createAccount(account);
    }

    private void printEntityInfo(Object entity) {
        for (Method method : entity.getClass().getMethods()) {
            if (method.getName().startsWith("get")) {
                try {
                    System.out.println("\t" + method.getName() + ": " + method.invoke(entity));
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
}
