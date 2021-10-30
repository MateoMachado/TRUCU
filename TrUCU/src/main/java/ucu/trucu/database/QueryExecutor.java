package ucu.trucu.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import ucu.trucu.database.querybuilder.statement.SelectStatement;
import ucu.trucu.database.querybuilder.statement.Statement;
import ucu.trucu.util.DBUtils;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

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

    public int execute(Statement statement) throws SQLException {
        try (java.sql.Statement sqlStatement = this.connection.createStatement()) {
            String statementStr = statement.build();
            LOGGER.query(statementStr);
            int updatedRows = sqlStatement.executeUpdate(statementStr);
            this.connection.commit();
            return updatedRows;
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

    public <T> List<T> query(SelectStatement selectStatement, Class<T> entityClass) throws SQLException, EntityConversionException {
        try (java.sql.Statement statement = this.connection.createStatement()) {
            String query = selectStatement.build();
            LOGGER.query(query);
            ResultSet results = statement.executeQuery(query);
            List<T> entityList = DBUtils.toEntityList(results, entityClass);
            LOGGER.info("<<< %s entidades [%s] halladas en tabla %s", entityList.size(), entityClass, selectStatement.getTable());
            return entityList;
        } catch (SQLException | EntityConversionException ex) {
            LOGGER.error(ex);
            throw ex;
        }
    }
}
