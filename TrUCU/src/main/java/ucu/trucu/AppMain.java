package ucu.trucu;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import ucu.trucu.util.log.ConsoleLog;
import ucu.trucu.util.log.FileLog;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@SpringBootApplication
public class AppMain {

    public static void main(String[] args) {

        // Configuracion de Logs del programa
        LoggerFactory.setProgramLogs(ConsoleLog::new, FileLog::new);

        // SpringBoot Init
        ConfigurableApplicationContext appContext = new SpringApplicationBuilder(AppMain.class)
                .headless(false)
                .run(args);
    }
}
