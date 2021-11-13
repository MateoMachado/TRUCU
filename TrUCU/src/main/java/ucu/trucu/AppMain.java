package ucu.trucu;

import java.io.IOException;
import java.net.URISyntaxException;
import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.EventListener;
import ucu.trucu.api.FrontController;
import ucu.trucu.database.DBController;
import ucu.trucu.util.log.ConsoleLog;
import ucu.trucu.util.log.FileLog;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@SpringBootApplication
public class AppMain {

    @Autowired
    private DBController dbController;

    @Autowired
    private FrontController frontController;

    public static void main(String[] args) {

        // Configuracion de Logs del programa
        LoggerFactory.setProgramLogs(ConsoleLog::new, FileLog::new);

        // SpringBoot Init
        ConfigurableApplicationContext appContext = new SpringApplicationBuilder(AppMain.class)
                .headless(false)
                .run(args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runApp() {
        try {
            dbController.initConnection();
            frontController.initFront();
        } catch (SQLException | IOException | URISyntaxException ex) {
            System.exit(0);
        }
    }
}
