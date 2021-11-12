package ucu.trucu.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import ucu.trucu.database.querybuilder.statement.InsertStatement;
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

    /**
     * Ejecuta una sentencia INSERT
     *
     * @param statement
     * @return Devuelve el id de la entidad autogenerado, o -1 si no hay ninguno
     * @throws SQLException
     */
    public List<Integer> executeInsert(InsertStatement statement) throws SQLException {
        try (java.sql.Statement sqlStatement = this.connection.createStatement()) {
            String statementStr = statement.build();
            LOGGER.query(statementStr);
            sqlStatement.executeUpdate(statementStr, java.sql.Statement.RETURN_GENERATED_KEYS);
            return DBUtils.getGeneratedId(sqlStatement.getGeneratedKeys());
        } catch (SQLException ex) {
            LOGGER.error(ex);
            throw ex;
        }
    }

    /**
     * Ejecuta una sentencia de actualizacion en la tabla (UPDATE, ALTER,
     * DELETE, TRUNCATE)
     *
     * @param statement
     * @return Devuelve la cantidad de entidades afectadas
     * @throws SQLException
     */
    public int executeUpdate(Statement statement) throws SQLException {
        try (java.sql.Statement sqlStatement = this.connection.createStatement()) {
            String statementStr = statement.build();
            LOGGER.query(statementStr);
            int updatedRows = sqlStatement.executeUpdate(statementStr);
            return updatedRows;
        } catch (SQLException ex) {
            LOGGER.error(ex);
            throw ex;
        }
    }

    /**
     * Ejecuta una sentencia SELECT
     *
     * @param <T> Clase de la entidad
     * @param selectStatement
     * @param entityClass
     * @return Devuelve una lista de entidades
     * @throws SQLException
     * @throws EntityConversionException
     */
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
