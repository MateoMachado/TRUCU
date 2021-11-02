package ucu.trucu.model.dao;

import org.springframework.stereotype.Component;
import ucu.trucu.model.dto.Rol;

/**
 *
 * @author NicoPuig
 */
@Component
public class RolDAO extends AbstractDAO<Rol> {

    @Override
    public String getTable() {
        return "Rol";
    }

    @Override
    public Class<Rol> getEntityClass() {
        return Rol.class;
    }

    @Override
    public Rol findByPK(Object... primaryKeys) {
        return findFirst(where -> where.eq("name", primaryKeys[0]));
    }
}
