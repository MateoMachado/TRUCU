package ucu.trucu.model.dao;

import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.database.querybuilder.statement.SelectStatement;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Offer.OfferStatus;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Publication.PublicationStatus;

/**
 *
 * @author NicoPuig
 */
@Component
public class PublicationDAO extends AbstractDAO<Publication> {

    public static final String PUBLICATION = "Publication";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String ACCOUNT_EMAIL = "accountEmail";
    public static final String UCUCOIN_VALUE = "ucuCoinValue";
    public static final String ID_PUBLICATION = "idPublication";
    public static final String PUBLICATION_DATE = "publicationDate";
    public static final String STATUS = "status";

    @Override
    public String getTable() {
        return PUBLICATION;
    }

    @Override
    public Class<Publication> getEntityClass() {
        return Publication.class;
    }

    @Override
    public Publication findByPK(Object... primaryKeys) {
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

        SelectStatement offeredPublications = QueryBuilder
                .selectFrom(OfferDAO.OFFERED_PUBLICATIONS, ID_PUBLICATION)
                .where(f -> f.eq(OfferDAO.ID_OFFER, idOffer));

        dbController.executeUpdate(
                QueryBuilder.update(getTable())
                        .set(STATUS, PublicationStatus.CLOSED)
                        .where(f -> f.in(ID_PUBLICATION, offeredPublications))
        );
    }

    public List<Publication> getOfferedPublications(int idOffer) {
        SelectStatement offeredPublicationsIds = QueryBuilder
                .selectFrom(OfferDAO.OFFERED_PUBLICATIONS, ID_PUBLICATION)
                .where(f -> f.eq(OfferDAO.ID_OFFER, idOffer));

        return dbController.executeQuery(
                QueryBuilder.selectFrom(getTable())
                        .where(filter -> filter.in(ID_PUBLICATION, offeredPublicationsIds)),
                getEntityClass()
        );
    }

    public PublicationStatus getStatus(int idPublication) {
        List<Publication> results = dbController.executeQuery(
                QueryBuilder
                        .selectFrom(getTable(), STATUS)
                        .where(filter -> filter.eq(ID_PUBLICATION, idPublication)),
                getEntityClass());
        return results.isEmpty() ? null : PublicationStatus.valueOf(results.get(0).getStatus());
    }

    public void reopenSettlingPublicationWithCanceledOffer(int idPublication) throws SQLException {
        SelectStatement offersWithPublication = QueryBuilder
                .selectFrom(OfferDAO.OFFERED_PUBLICATIONS, OfferDAO.ID_OFFER)
                .where(where -> where.eq(OfferDAO.OFFERED_PUBLICATIONS + "." + ID_PUBLICATION, idPublication));

        SelectStatement publicationsWithOffers = QueryBuilder
                .selectDistinctFrom(OfferDAO.OFFER, ID_PUBLICATION)
                .where(where
                        -> where.and(
                        where.in(OfferDAO.ID_OFFER, offersWithPublication),
                        where.eq(OfferDAO.STATUS, OfferStatus.SETTLING)));

        dbController.executeUpdate(
                QueryBuilder.update(getTable())
                        .set(STATUS, PublicationStatus.OPEN)
                        .where(where -> where.in(ID_PUBLICATION, publicationsWithOffers))
        );
    }
}
