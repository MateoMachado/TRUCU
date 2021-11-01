package ucu.trucu.model.dao;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Offer.OfferStatus;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Publication.PublicationStatus;
import ucu.trucu.util.DBUtils;

/**
 *
 * @author NicoPuig
 */
@Component
public class OfferDAO extends AbstractDAO<Offer> {

    @Override
    public String getTable() {
        return "Offer";
    }

    @Override
    public Class<Offer> getEntityClass() {
        return Offer.class;
    }

    @Override
    public Offer findByPK(String... primaryKeys) {
        return findFirst(where -> where.eq("idOffer", primaryKeys[0]));
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
                QueryBuilder.insertInto("OfferedPublications")
                        .keyValue(ids)
        );
    }

    public void deleteOfferedPublications(int idOffer, Function<Filter, String> filter) throws SQLException {
        dbController.executeUpdate(
                QueryBuilder.deleteFrom("OfferedPublications")
                        .where(filter)
        );
    }

    public List<Offer> getUserPublications(int idUser) {
        return dbController.executeQuery(QueryBuilder
                .selectFrom(getTable(), true, getTable() + ".*")
                .joinOn("OfferedPublications",
                        "OfferedPublications.idOffer = Offer.idOffer")
                .joinOn("Publication",
                        "Publication.idPublication = OfferedPublications.idPublication")
                .where(where -> where.eq("Publication.accountCI", idUser)),
                getEntityClass()
        );
    }

    public void closeOffer(int idPublication, int idOffer) throws SQLException {
        dbController.executeUpdate(
                QueryBuilder.update(getTable())
                        .where(filter -> filter.eq("idPublication", idPublication))
                        .set("Offer.status", cases
                                -> cases
                                .addCase(when -> when.eq("idOffer", idOffer), OfferStatus.CLOSED)
                                .orElse(OfferStatus.REJECTED))
        );
    }
}
