package ucu.trucu.model.dao;

import java.util.List;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.model.dto.Reason;

/**
 *
 * @author NicoPuig
 */
@Component
public class ReasonDAO extends AbstractDAO<Reason> {

    public static final String REASON = "Reason";
    public static final String ID_REASON = "idReason";
    
    @Override
    public String getTable() {
        return REASON;
    }

    @Override
    public Class<Reason> getEntityClass() {
        return Reason.class;
    }

    @Override
    public Reason findByPK(Object... primaryKeys) {
        return findFirst(where -> where.eq(ID_REASON, primaryKeys[0]));
    }
    
    public List<Reason> getReasons() {
        return dbController.executeQuery(
                QueryBuilder.selectFrom(REASON)
                , getEntityClass()
        );
    }
}
