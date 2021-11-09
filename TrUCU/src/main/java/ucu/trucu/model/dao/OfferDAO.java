package ucu.trucu.model.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.database.querybuilder.statement.SelectStatement;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Offer.OfferStatus;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.util.DBUtils;

/**
 *
 * @author NicoPuig
 */
@Component
public class OfferDAO extends AbstractDAO<Offer> {

    public static final String ID_OFFER = "idOffer";
    public static final String STATUS = "status";
    public static final String ID_PUBLICATION = "idPublication";
    public static final String OFFER_DATE = "offerDate";
    public static final String OFFERED_PUBLICATIONS = "OfferedPublications";

    @Override
    public String getTable() {
        return "Offer";
    }

    @Override
    public Class<Offer> getEntityClass() {
        return Offer.class;
    }

    @Override
    public Offer findByPK(Object... primaryKeys) {
        return findFirst(where -> where.eq(ID_OFFER, primaryKeys[0]));
    }

    public void addOfferedPublications(int idOffer, int idPublication) throws SQLException {
        // Creo una oferta y una publicación unicamente con los ID
        Offer offerID = new Offer();
        offerID.setIdOffer(idOffer);
        Publication publication = new Publication();
        publication.setIdPublication(idPublication);

        // Mapeo los atributos
        Map<String, Object> ids = DBUtils.objectToPropertyMap(publication);
        ids.putAll(DBUtils.objectToPropertyMap(offerID));

        dbController.executeInsert(
                QueryBuilder.insertInto(OFFERED_PUBLICATIONS)
                        .keyValue(ids)
        );
    }

    public void deleteOfferedPublications(int idOffer, Function<Filter, String> filter) throws SQLException {
        dbController.executeUpdate(
                QueryBuilder.deleteFrom(OFFERED_PUBLICATIONS)
                        .where(filter)
        );
    }

    public List<Offer> getUserPublications(int accountEmail) {
        return dbController.executeQuery(QueryBuilder
                .selectDistinctFrom(getTable(), getTable() + ".*")
                .joinOn(OFFERED_PUBLICATIONS,
                        "OfferedPublications.idOffer = Offer.idOffer")
                .joinOn(PublicationDAO.PUBLICATION,
                        "Publication.idPublication = OfferedPublications.idPublication")
                .where(where -> where.eq("Publication.accountEmail", accountEmail)),
                getEntityClass()
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

    public void rejectOffersToPublicationsOf(int idOffer) throws SQLException {

        SelectStatement offeredPublicationsQuery = QueryBuilder
                .selectFrom(OFFERED_PUBLICATIONS, ID_PUBLICATION)
                .where(f -> f.eq("OfferedPublications.idOffer", idOffer));

        dbController.executeUpdate(
                QueryBuilder.update(getTable())
                        .set(STATUS, OfferStatus.REJECTED)
                        .where(f -> f.in(ID_PUBLICATION, offeredPublicationsQuery))
        );
    }

    public void cancelOffersWithPublicationsOf(int idPublication, int idOffer) throws SQLException {

        SelectStatement offeredPublicationsQuery = QueryBuilder
                .selectFrom(OFFERED_PUBLICATIONS, ID_PUBLICATION)
                .where(f -> f.eq("OfferedPublications.idOffer", idOffer));

        SelectStatement offersWithClosedOfferPublicationsQuery = QueryBuilder
                .selectDistinctFrom(OFFERED_PUBLICATIONS, ID_OFFER)
                .where(f
                        -> f.and(
                        // No se cancela la oferta cerrada
                        f.notEq("OfferedPublications.idOffer", idOffer),
                        f.or(
                                // Se cancelan las ofertas con la publicacion cerrada
                                f.eq("OfferedPublications.idPublication", idPublication),
                                // Se cancelan las publicaciones ofrecidas en la oferta cerrada
                                f.in("OfferedPublications.idPublication", offeredPublicationsQuery)
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

    public int countOffer(Filter filter) {
        return dbController.executeQuery(
                QueryBuilder.selectFrom(getTable(), ID_OFFER)
                        .where(filter),
                getEntityClass())
                .size();
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
