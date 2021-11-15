package ucu.trucu.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.database.querybuilder.statement.InsertStatement;
import ucu.trucu.database.querybuilder.statement.SelectStatement;
import static ucu.trucu.model.dao.PublicationDAO.ID_PUBLICATION;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Offer.OfferStatus;

/**
 *
 * @author NicoPuig
 */
@Component
public class OfferDAO extends AbstractDAO<Offer> {

    public static final String OFFER = "Offer";
    public static final String ID_OFFER = "idOffer";
    public static final String STATUS = "status";
    public static final String OFFER_DATE = "offerDate";
    public static final String OFFERED_PUBLICATIONS = "OfferedPublications";

    @Override
    public String getTable() {
        return OFFER;
    }

    @Override
    public Class<Offer> getEntityClass() {
        return Offer.class;
    }

    @Override
    public Offer findByPK(Object... primaryKeys) {
        return findFirst(where -> where.eq(ID_OFFER, primaryKeys[0]));
    }

    public void addOfferedPublications(int idOffer, List<Integer> idPublications) throws SQLException {
        InsertStatement insert = QueryBuilder
                .insertInto(OFFERED_PUBLICATIONS)
                .keys(ID_OFFER, ID_PUBLICATION);

        idPublications.forEach(idPublication -> insert.values(idOffer, idPublication));
        dbController.executeInsert(insert);
    }

    public void deleteOfferedPublications(Function<Filter, String> filter) throws SQLException {
        dbController.executeUpdate(
                QueryBuilder.deleteFrom(OFFERED_PUBLICATIONS)
                        .where(filter)
        );
    }

    public void closeOfferToPublicationAndRejectOthers(int idPublication, int closedOfferId) throws SQLException {
        dbController.executeUpdate(QueryBuilder
                .update(getTable())
                .where(filter -> filter.eq(ID_PUBLICATION, idPublication))
                .set(STATUS, cases
                        -> cases
                        .forParam(ID_OFFER)
                        .addCase(closedOfferId, OfferStatus.CLOSED)
                        .orElse(OfferStatus.REJECTED)
                )
        );
    }

    public void rejectOffersToPublication(int idPublication) throws SQLException {
        dbController.executeUpdate(
                QueryBuilder.update(getTable())
                        .set(STATUS, OfferStatus.REJECTED)
                        .where(f -> f.eq(ID_PUBLICATION, idPublication))
        );
    }

    public void rejectOffersToPublicationsOf(int idOffer) throws SQLException {

        SelectStatement offeredPublicationsQuery = QueryBuilder
                .selectFrom(OFFERED_PUBLICATIONS, ID_PUBLICATION)
                .where(f -> f.eq(OFFERED_PUBLICATIONS + "." + ID_OFFER, idOffer));

        dbController.executeUpdate(
                QueryBuilder.update(getTable())
                        .set(STATUS, OfferStatus.REJECTED)
                        .where(f -> f.in(ID_PUBLICATION, offeredPublicationsQuery))
        );
    }

    public void cancelOffersWithPublication(int idPublication) throws SQLException {

        SelectStatement offersWithPublication = QueryBuilder
                .selectDistinctFrom(OFFERED_PUBLICATIONS, ID_OFFER)
                .where(f -> f.eq(OFFERED_PUBLICATIONS + "." + ID_PUBLICATION, idPublication));

        dbController.executeUpdate(
                QueryBuilder.update(getTable())
                        .set(STATUS, OfferStatus.CANCELED)
                        .where(f -> f.in(ID_OFFER, offersWithPublication))
        );
    }

    public void cancelOffersWithPublicationsOf(int idPublication, int idOffer) throws SQLException {

        SelectStatement offeredPublicationsQuery = QueryBuilder
                .selectFrom(OFFERED_PUBLICATIONS, ID_PUBLICATION)
                .where(f -> f.eq(OFFERED_PUBLICATIONS + "." + ID_OFFER, idOffer));

        SelectStatement offersWithClosedOfferPublicationsQuery = QueryBuilder
                .selectDistinctFrom(OFFERED_PUBLICATIONS, ID_OFFER)
                .where(f
                        -> f.and(
                        // No se cancela la oferta cerrada
                        f.notEq(OFFERED_PUBLICATIONS + "." + ID_OFFER, idOffer),
                        f.or(
                                // Se cancelan las ofertas con la publicacion cerrada
                                f.eq(OFFERED_PUBLICATIONS + "." + ID_PUBLICATION, idPublication),
                                // Se cancelan las publicaciones ofrecidas en la oferta cerrada
                                f.in(OFFERED_PUBLICATIONS + "." + ID_PUBLICATION, offeredPublicationsQuery)
                        )));

        dbController.executeUpdate(
                QueryBuilder.update(getTable())
                        .set(STATUS, OfferStatus.CANCELED)
                        .where(f -> f.in(ID_OFFER, offersWithClosedOfferPublicationsQuery))
        );
    }

    public OfferStatus getStatus(int idOffer) {
        List<Offer> results = dbController.executeQuery(
                QueryBuilder
                        .selectFrom(getTable(), STATUS)
                        .where(filter -> filter.eq(ID_OFFER, idOffer)),
                getEntityClass());
        return results.isEmpty() ? null : OfferStatus.valueOf(results.get(0).getStatus());
    }

    public int getOfferPublicationId(int idOffer) {
        List<Offer> results = dbController.executeQuery(
                QueryBuilder
                        .selectFrom(getTable(), ID_PUBLICATION)
                        .where(filter -> filter.eq(ID_OFFER, idOffer)),
                getEntityClass());
        return results.isEmpty() ? null : results.get(0).getIdPublication();
    }

    public List<Offer> filterOffers(int pageSize, int pageNumber, Filter filter) {
        return dbController.executeQuery(
                QueryBuilder.selectFrom(getTable())
                        .where(filter)
                        .orderDesc(OFFER_DATE)
                        .offset(pageSize * pageNumber)
                        .fetchNext(pageSize),
                getEntityClass()
        );
    }
}
