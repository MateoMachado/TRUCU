package ucu.trucu.database;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import ucu.trucu.database.querybuilder.statement.InsertStatement;
import ucu.trucu.database.querybuilder.statement.SelectStatement;
import ucu.trucu.database.querybuilder.statement.Statement;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@Controller
public class DBController {

    private static final Logger LOGGER = LoggerFactory.create(DBController.class);

    private Connection connection;
    private QueryExecutor queryExecutor;

    @Value("${trucu.db.url}")
    private String dbURL;

    @Value("${trucu.db.user}")
    private String dbUser;

    @Value("${trucu.db.password}")
    private String dbPassword;

    public void initConnection() throws SQLException {
        try {
            LOGGER.info("Iniciando conexion con base de datos [%s]", dbURL);
            connection = DriverManager.getConnection(dbURL, dbUser, dbPassword);
            connection.setAutoCommit(false);
            queryExecutor = new QueryExecutor(connection);
            LOGGER.info("Base de datos conectada con exito");
        } catch (HeadlessException | SQLException e) {
            LOGGER.error("Conexion a base de datos fallida -> %s", e.getMessage());
            throw e;
        }
    }

    @PreDestroy
    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                LOGGER.info("Conexion a base de datos cerrada con exito");
            } catch (SQLException ex) {
                LOGGER.error("Imposible cerrar conexion con BD -> %s", ex);
            }
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

    public int executeUpdate(Statement statement) throws SQLException {
        return queryExecutor.executeUpdate(statement);
    }

    public List<Integer> executeInsert(InsertStatement statement) throws SQLException {
        return queryExecutor.executeInsert(statement);
    }

    public void commit() throws SQLException {
        this.connection.commit();
    }

    public void rollback() {
        try {
            this.connection.rollback();
        } catch (SQLException ex) {
            LOGGER.error("IMPOSIBLE REALIZAR ROLLACK -> ", ex);
        }
    }
}
