package ucu.trucu;

import org.springframework.beans.factory.annotation.Autowired;
import ucu.trucu.api.AccountController;

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
    private AccountController accountController;

//    @EventListener(ApplicationReadyEvent.class)
    public void start() {

    }
}
