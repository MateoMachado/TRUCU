package trucu;

import java.sql.SQLException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import trucu.database.DBController;
import trucu.util.log.ConsoleLog;
import trucu.util.log.FileLog;
import trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@SpringBootApplication
public class AppMain {

    // TODO: Archivo de configuracion
    private static final String USER_BD = "trucu";
    private static final String PASSWORD_BD = "bdtrucu123";
    private static final String URL_BD = "jdbc:sqlserver://localhost:1433;databaseName=Trucu";

    public static void main(String[] args) {

        // Configuracion de Logs del programa
        LoggerFactory.setProgramLogs(ConsoleLog::new, FileLog::new);

        // Conexion con BD
        try {
            DBController.initConnection(USER_BD, PASSWORD_BD, URL_BD);
        } catch (SQLException ex) {
            System.out.println(ex);
        }

        // SpringBoot Init
        AnnotationConfigApplicationContext annotationContext = new AnnotationConfigApplicationContext(AppMain.class);
        annotationContext.scan(AppMain.class.getPackageName());

        ConfigurableApplicationContext appContext = new SpringApplicationBuilder(AppMain.class)
                .headless(false)
                .run(args);
    }
}
