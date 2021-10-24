package trucu.database;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import trucu.database.querybuilder.statement.SelectStatement;
import trucu.database.querybuilder.statement.Statement;
import trucu.database.querybuilder.statement.UpdateStatement;
import trucu.util.log.Logger;
import trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
public class DBController {

    private static final Logger LOGGER = LoggerFactory.create(DBController.class);

    private final Connection connection;
    private final QueryExecutor queryExecutor;

    private DBController(Connection connection) {
        this.connection = connection;
        this.queryExecutor = new QueryExecutor(connection);
    }

    public static DBController initConnection(String BDUser, String BDPassword, String BDUrl) throws SQLException {
        try {
            Connection connection = DriverManager.getConnection(BDUrl, BDUser, BDPassword);
            connection.setAutoCommit(false);
            DBController controller = new DBController(connection);
            LOGGER.info("Base de datos conectada con exito");
            return controller;
        } catch (HeadlessException | SQLException e) {
            LOGGER.error("Imposible conectar a BD -> %s", e.getMessage());
            LOGGER.popUp("Imposible conectar a BD \n-> %s", e.getMessage());
            throw e;
        }
    }

    public void closeConnection() {
        try {
            this.connection.close();
        } catch (SQLException ex) {
            LOGGER.error("Imposible cerrar conexion con BD -> %s", ex);
        }
    }

    public Table executeQuery(SelectStatement query) {
        try {
            return this.queryExecutor.query(query);
        } catch (SQLException ex) {
            LOGGER.error("Imposible ejecutar query");
            return null;
        }
    }

    public <T> List<T> executeQuery(SelectStatement query, Class<T> entityClass) {
        try {
            return this.queryExecutor.query(query, entityClass);
        } catch (SQLException | EntityConversionException ex) {
            LOGGER.error("Imposible ejecutar query");
            return null;
        }
    }

    public boolean executeStatement(Statement statement) {
        try {
            this.queryExecutor.execute(statement);
            return true;
        } catch (SQLException ex) {
            LOGGER.error("Imposible ejecutar query");
            return false;
        }
    }

    public boolean updateValues(UpdateStatement statement) {
        try {
            LOGGER.info("Actualizando valores en tabla [%s]...", statement.getTable());
            this.queryExecutor.execute(statement);
            LOGGER.info("Valores actualizados!");
            return true;
        } catch (SQLException ex) {
            LOGGER.error("Imposible actualizar valores en tabla [%s]", statement.getTable());
            return false;
        }
    }
}
