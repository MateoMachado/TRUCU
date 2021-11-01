package ucu.trucu.model.dao;

import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Publication.PublicationStatus;

/**
 *
 * @author NicoPuig
 */
@Component
public class PublicationDAO extends AbstractDAO<Publication> {

    private static final String ID_PUBLICATION = "idPublication";
    private static final String PUBLICATION_DATE = "publicationDate";

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
        return findFirst(where -> where.eq(ID_PUBLICATION, primaryKeys[0]));
    }

    public List<Publication> filterPublications(int pageSize, int pageNumber, Filter filter) {
        return dbController.executeQuery(
                QueryBuilder.selectFrom(getTable())
                        .where(filter)
                        .orderDesc(PUBLICATION_DATE)
                        .offset(pageSize * pageNumber)
                        .fetchNext(pageSize),
                getEntityClass()
        );
    }

    public void closeOfferPublications(int idOffer) throws SQLException {
        dbController.executeUpdate(
                QueryBuilder.update(getTable())
                        .joinOn("OfferedPublications", "OfferedPublications.idPublication = Publication.idPublication")
                        .joinOn("Offer", "OfferedPublications.idOffer = Offer.idOffer")
                        .set(getTable() + ".status", PublicationStatus.CLOSED)
                        .where(filter -> filter.eq("Offer.idOffer", idOffer))
        );
    }
}
