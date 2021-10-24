package trucu;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import trucu.database.DBController;
import trucu.database.EntityConversionException;
import trucu.database.Table;
import trucu.database.querybuilder.QueryBuilder;
import trucu.model.dto.Account;
import trucu.util.StringUtils;
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

        // SpringBoot Init
        ConfigurableApplicationContext context = new SpringApplicationBuilder(AppMain.class)
                .headless(false)
                .run(args);

        // Configuracion de Logs del programa
        LoggerFactory.setProgramLogs(ConsoleLog::new, FileLog::new);

        // Conexion con BD
        try {
            DBController dbController = DBController.initConnection(USER_BD, PASSWORD_BD, URL_BD);
            List<Account> entities = dbController.executeQuery(QueryBuilder.selectFrom("Account"), Account.class);
            System.out.println(StringUtils.join(StringUtils.COMA_LN, entities, Account::getCI));
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
}
