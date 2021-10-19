package trucu.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import trucu.database.querybuilder.statement.SelectStatement;
import trucu.database.querybuilder.statement.Statement;
import trucu.util.log.Logger;
import trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
public class QueryExecutor {

    private final static Logger LOGGER = LoggerFactory.create(QueryExecutor.class);

    private final Connection connection;

    public QueryExecutor(Connection connection) {
        this.connection = connection;
    }

    public void update(Statement updateStatement) throws SQLException {
        try (java.sql.Statement statement = this.connection.createStatement()) {
            String updateQuery = updateStatement.build();
            LOGGER.query(updateQuery);
            statement.executeUpdate(updateQuery);
            this.connection.commit();
        } catch (SQLException ex) {
            this.connection.rollback();
            LOGGER.error(ex);
            throw ex;
        }
    }

    public Table query(SelectStatement selectStatement) throws SQLException {
        try (java.sql.Statement statement = this.connection.createStatement()) {
            String query = selectStatement.build();
            LOGGER.query(query);
            ResultSet results = statement.executeQuery(query);
            Table table = Table.toTable(results);
            LOGGER.info("<<< %sx%s datos hallados en tabla", table.getRowCount(), table.getColumnCount());
            return table;
        } catch (SQLException ex) {
            LOGGER.error(ex);
            throw ex;
        }
    }
}
