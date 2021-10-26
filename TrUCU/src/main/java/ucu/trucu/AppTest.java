package ucu.trucu;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.model.dao.ReasonDAO;

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
@Component
public class AppTest {

    @Autowired
    private ReasonDAO dao;

    @EventListener(ApplicationReadyEvent.class)
    public void start() {
        List select = dao.select(QueryBuilder.selectFrom(dao.getTable()));
        int i = 1;
        for (Object obj : select) {
            System.out.println("Object" + i++);
            for (Method method : dao.getEntityClass().getMethods()) {
                if (method.getName().startsWith("get")) {
                    try {
                        System.out.println("\t" + method.getName() + ": " + method.invoke(obj));
                    } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                        System.out.println(ex);
                    }
                }
            }
        }
    }
}
