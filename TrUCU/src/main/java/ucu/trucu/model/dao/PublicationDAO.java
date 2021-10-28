package ucu.trucu.model.dao;

import org.springframework.stereotype.Component;
import ucu.trucu.model.dto.Publication;

/**
 *
 * @author NicoPuig
 */
@Component
public class PublicationDAO extends AbstractDAO<Publication> {

    @Override
    public String getTable() {
        return "Publication";
    }

    @Override
    public Class<Publication> getEntityClass() {
        return Publication.class;
    }

    @Override
    public Publication findByPK(String... primaryKeys) {
        return findFirst(where -> where.eq("idPublication", primaryKeys[0]));
    }
}
