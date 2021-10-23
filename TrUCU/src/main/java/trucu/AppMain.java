package trucu;

import java.sql.SQLException;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import trucu.database.DBController;
import trucu.database.SQLType;
import trucu.database.querybuilder.Filter;
import trucu.database.querybuilder.ForeignTableKey;
import trucu.database.querybuilder.ForeignTableKey.ON_DELETE_ACTION;
import trucu.database.querybuilder.QueryBuilder;
import trucu.database.querybuilder.statement.Statement;
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
        String build = QueryBuilder.createTable("table")
                .addColumn("id", SQLType.BIGINT, 20)
                .addColumn("col1", SQLType.INT)
                .addColumn("col2", SQLType.DOUBLE, 24)
                .addColumn("table2Id", SQLType.INT, 20)
                .setCheck(f -> f.goet("col1", 50))
                .setDefaultValue("col2", 100.00)
                .setPrimaryKey("id")
                .setForeignKey("table2Id", "id", "table2", ON_DELETE_ACTION.CASCADE)
                .setNotNull("col2", true)
                .setUniqueKey("col1", "col2")
                .build();

        System.out.println(build);
    }
}
