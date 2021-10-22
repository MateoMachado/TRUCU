package trucu;

import java.sql.SQLException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import trucu.database.DBController;
import trucu.database.SQLType;
import trucu.database.querybuilder.Filter;
import trucu.database.querybuilder.QueryBuilder;
import trucu.database.querybuilder.statement.AlterTableStatement;
import trucu.database.querybuilder.statement.Statement;
import trucu.database.querybuilder.statement.UpdateStatement;
import trucu.util.log.ConsoleLog;
import trucu.util.log.FileLog;
import trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@SpringBootApplication
public class AppMain {

    private static final String USER_BD = "Bd-trucu";
    private static final String PASSWORD_BD = "Kn5N&!B*";
    private static final String URL_BD = "jdbc:sqlserver://198.168.0.3:5432";

    public static void main(String[] args) {

        // SpringBoot Init
        ConfigurableApplicationContext context = new SpringApplicationBuilder(AppMain.class)
                .headless(false)
                .run(args);

        // Configuracion de Logs del programa
        LoggerFactory.setProgramLogs(ConsoleLog::new, FileLog::new);

        test();

        // Conexion con BD
        try {
            DBController.initConnection(USER_BD, PASSWORD_BD, URL_BD);
        } catch (SQLException ex) {
        }
    }

    private static void test() {
        Statement update = QueryBuilder.deleteFrom("table1")
                .where(Filter.build(f -> f.and(
                f.in("col1", 1, 2, 3, 4, 5),
                f.exists(QueryBuilder.selectFrom("table2")
                        .where(Filter.build(f2 -> f2.isNotNull("col5")))),
                f.like("name", "miguel")
        )));

        System.out.println(update.build());
    }
}
