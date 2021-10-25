package ucu.trucu.model.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import ucu.trucu.database.DBController;
import ucu.trucu.database.querybuilder.statement.SelectStatement;
import ucu.trucu.database.querybuilder.statement.UpdateStatement;

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

    public List<T> select(SelectStatement statement) {
        return dbController.executeQuery(statement, getEntityClass());
    }

    public void updateValues(UpdateStatement statement) {
        dbController.executeUpdate(statement);
    }
}
