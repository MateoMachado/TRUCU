package trucu.database;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Controller;
import trucu.database.querybuilder.statement.SelectStatement;
import trucu.database.querybuilder.statement.Statement;
import trucu.database.querybuilder.statement.UpdateStatement;
import trucu.util.log.Logger;
import trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@Controller
public class DBController {

    private static final Logger LOGGER = LoggerFactory.create(DBController.class);
    private static Connection connection;
    private static QueryExecutor queryExecutor;

    public static void initConnection(String BDUser, String BDPassword, String BDUrl) throws SQLException {
        try {
            connection = DriverManager.getConnection(BDUrl, BDUser, BDPassword);
            connection.setAutoCommit(false);
            queryExecutor = new QueryExecutor(connection);
            LOGGER.info("Base de datos conectada con exito");
        } catch (HeadlessException | SQLException e) {
            LOGGER.error("Imposible conectar a BD -> %s", e.getMessage());
            LOGGER.popUp("Imposible conectar a BD \n-> %s", e.getMessage());
            throw e;
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            LOGGER.error("Imposible cerrar conexion con BD -> %s", ex);
        }
    }

    public Table executeQuery(SelectStatement query) {
        try {
            return queryExecutor.query(query);
        } catch (SQLException ex) {
            LOGGER.error("Imposible ejecutar query");
            return null;
        }
    }

    public <T> List<T> executeQuery(SelectStatement query, Class<T> entityClass) {
        try {
            return queryExecutor.query(query, entityClass);
        } catch (SQLException | EntityConversionException ex) {
            LOGGER.error("Imposible ejecutar query");
            return null;
        }
    }

    public boolean executeStatement(Statement statement) {
        try {
            queryExecutor.execute(statement);
            return true;
        } catch (SQLException ex) {
            LOGGER.error("Imposible ejecutar query");
            return false;
        }
    }

    public boolean executeUpdate(UpdateStatement statement) {
        try {
            LOGGER.info("Actualizando valores en tabla [%s]...", statement.getTable());
            queryExecutor.execute(statement);
            LOGGER.info("Valores actualizados!");
            return true;
        } catch (SQLException ex) {
            LOGGER.error("Imposible actualizar valores en tabla [%s]", statement.getTable());
            return false;
        }
    }
}
