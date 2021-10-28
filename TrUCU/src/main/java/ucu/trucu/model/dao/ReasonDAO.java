package ucu.trucu.model.dao;

import org.springframework.stereotype.Component;
import ucu.trucu.model.dto.Reason;

/**
 *
 * @author NicoPuig
 */
@Component
public class ReasonDAO extends AbstractDAO<Reason> {

    @Override
    public String getTable() {
        return "Reason";
    }

    @Override
    public Class<Reason> getEntityClass() {
        return Reason.class;
    }

    @Override
    public Reason findByPK(String... primaryKeys) {
        return findFirst(where -> where.eq("idReason", primaryKeys[0]));
    }
}
