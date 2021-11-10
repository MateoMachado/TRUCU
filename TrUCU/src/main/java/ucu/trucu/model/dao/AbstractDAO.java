package ucu.trucu.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;
import org.springframework.beans.factory.annotation.Autowired;
import ucu.trucu.database.DBController;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.database.querybuilder.Count;
import ucu.trucu.util.DBUtils;

/**
 *
 * @author NicoPuig
 * @param <T>
 */
public abstract class AbstractDAO<T> {

    @Autowired
    protected DBController dbController;

    public abstract String getTable();

    public abstract Class<T> getEntityClass();

    public abstract T findByPK(Object... primaryKeys);

    public List<T> findBy(Function<Filter, String> filter) {
        return dbController.executeQuery(
                QueryBuilder.selectFrom(getTable())
                        .where(filter), getEntityClass()
        );
    }

    public T findFirst(Function<Filter, String> filter) {
        List<T> entities = findBy(filter);
        return entities.isEmpty() ? null : entities.get(0);
    }

    public int update(T entity, Function<Filter, String> filter) throws SQLException {
        return dbController.executeUpdate(
                QueryBuilder.update(this.getTable())
                        .set(DBUtils.objectToPropertyMap(entity))
                        .where(filter)
        );
    }

    public int insert(T newEntity) throws SQLException {
        return dbController.executeInsert(
                QueryBuilder.insertInto(this.getTable())
                        .keyValue(DBUtils.objectToPropertyMap(newEntity))
        ).get(0);
    }

    public int delete(Function<Filter, String> filter) throws SQLException {
        return dbController.executeUpdate(
                QueryBuilder.deleteFrom(getTable())
                        .where(filter)
        );
    }

    public int count(Filter filter) {
        List<Count> counts = dbController.executeQuery(QueryBuilder
                .countFrom(getTable())
                .where(filter),
                Count.class);
        return !counts.isEmpty() && counts.get(0) != null ? (int) counts.get(0).getCount() : 0;
    }
}
