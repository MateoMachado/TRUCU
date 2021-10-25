package trucu.model.dao;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trucu.database.DBController;
import trucu.database.querybuilder.statement.SelectStatement;
import trucu.database.querybuilder.statement.UpdateStatement;

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
