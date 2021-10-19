package trucu;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * @author NicoPuig
 */
@SpringBootApplication
public class AppMain {

    public static void main(String[] args) {

        ConfigurableApplicationContext context = new SpringApplicationBuilder(AppMain.class)
                .headless(false)
                .run(args);

    }
}
