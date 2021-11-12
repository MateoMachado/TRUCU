package ucu.trucu;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.sql.SQLException;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;
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
    private App app;

    public static void main(String[] args) {

        // Configuracion de Logs del programa
        LoggerFactory.setProgramLogs(ConsoleLog::new, FileLog::new);

        // SpringBoot Init
        ConfigurableApplicationContext appContext = new SpringApplicationBuilder(AppMain.class)
                .headless(false)
                .run(args);
    }

    @Component
    public class App {

        @Autowired
        private DBController dbController;

        @Value("${trucu.front.url.firebase}")
        private URI frontURL;

        @Value("${trucu.front.open}")
        private boolean autoOpenFront;

        @PostConstruct
        public void start() {
            try {
                dbController.initConnection();
                if (autoOpenFront) {
                    Desktop.getDesktop().browse(frontURL);
                }
            } catch (SQLException | IOException ex) {
                System.err.println(ex);
                System.exit(1);
            }
        }
    }
}
