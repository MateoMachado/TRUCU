package ucu.trucu.helper;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.dao.OfferDAO;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Offer.OfferStatus;
import ucu.trucu.model.dto.Publication.PublicationStatus;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author NicoPuig
 */
@Service
public class OfferHelper {

    private static final Logger LOGGER = LoggerFactory.create(OfferHelper.class);
    private static final String ID_OFFER = "idOffer";

    @Autowired
    private OfferDAO offerDAO;

    @Autowired
    private PublicationHelper publicationHelper;

    public void createOffer(Offer newOffer, List<Integer> idPublications) throws SQLException {
        int idOffer = offerDAO.insert(newOffer);

        for (int idPublication : idPublications) {
            offerDAO.addOfferedPublications(idOffer, idPublication);
        }
    }

    public void deleteOffer(int idOffer) throws SQLException {
        // Elimino las publicaciones relacionadas con la oferta
        offerDAO.deleteOfferedPublications(idOffer, where
                -> where.eq(ID_OFFER, idOffer));

        // Elimino la oferta
        offerDAO.delete(where -> where.eq(ID_OFFER, idOffer));
    }

    public List<Offer> getUserOffers(int idUser) {
        return offerDAO.getUserPublications(idUser);
    }

    public void closeOffer(int idOffer) throws SQLException {

        LOGGER.info("Validando estados para cerrar oferta [idOffer=%s]", idOffer);
        this.assertStatus(idOffer, OfferStatus.SETTLING);
        int idPublication = offerDAO.getOfferPublicationId(idOffer);
        publicationHelper.assertStatus(idPublication, PublicationStatus.SETTLING);

        LOGGER.info("Cerrando publicacion principal [idPublication=%s]...", idPublication);
        publicationHelper.changePublicationStatus(idPublication, PublicationStatus.CLOSED);

        LOGGER.info("Cerrando oferta aceptada [idOffer=%s], y rechazando todas las otras ofertas a la publicacion [idPublication=%s]...", idOffer, idPublication);
        offerDAO.closeOfferToPublicationAndRejectOthers(idPublication, idOffer);

        LOGGER.info("Cerrando publicacion ofrecidas en la oferta [idOffer=%s]...", idOffer);
        publicationHelper.closeOfferPublications(idOffer);

        LOGGER.info("Rechazando ofertas realizadas a publicaciones de la oferta [idOffer=%s]...", idOffer);
        offerDAO.rejectOffersToPublicationsOf(idOffer);

        LOGGER.info("Cancelando ofertas con las publicaciones de la oferta [idOffer=%s]...", idOffer);
        offerDAO.cancelOffersWithPublicationsOf(idPublication, idOffer);
    }

    public void acceptOffer(int idOffer) throws SQLException {

        LOGGER.info("Validando estados para aceptar oferta [idOffer=%s]", idOffer);
        this.assertStatus(idOffer, OfferStatus.OPEN);
        int idPublication = offerDAO.getOfferPublicationId(idOffer);
        publicationHelper.assertStatus(idPublication, PublicationStatus.OPEN);

        LOGGER.info("Aceptando oferta [idOffer=%s] en publicacion [idPublication=%s]...", idOffer, idPublication);
        publicationHelper.changePublicationStatus(idPublication, PublicationStatus.SETTLING);

        LOGGER.info("Aceptando oferta [idOffer=%s]...", idOffer, idPublication);
        changeOfferStatus(idOffer, OfferStatus.SETTLING);
    }

    public void cancelOffer(int idOffer) throws SQLException {

        LOGGER.info("Validando estados para cancelar oferta [idOffer=%s]", idOffer);
        this.assertStatusNotEqual(idOffer, OfferStatus.CLOSED);

        OfferStatus offerStatus = offerDAO.getStatus(idOffer);
        if (OfferStatus.SETTLING.equals(offerStatus)) {
            int idPublication = offerDAO.getOfferPublicationId(idOffer);
            publicationHelper.changePublicationStatus(idPublication, PublicationStatus.OPEN);
        }
        changeOfferStatus(idOffer, OfferStatus.CANCELED);
    }

    public void revertAcceptance(int idOffer) throws SQLException {

        LOGGER.info("Validando estados para revertir aceptacion de oferta [idOffer=%s]", idOffer);
        this.assertStatus(idOffer, OfferStatus.SETTLING);
        int idPublication = offerDAO.getOfferPublicationId(idOffer);
        publicationHelper.assertStatus(idPublication, PublicationStatus.SETTLING);

        publicationHelper.changePublicationStatus(idPublication, PublicationStatus.OPEN);
        changeOfferStatus(idOffer, OfferStatus.OPEN);
    }

    public void rejectOffer(int idOffer) throws SQLException {

        LOGGER.info("Validando estados para rechazar oferta [idOffer=%s]", idOffer);
        this.assertStatus(idOffer, OfferStatus.OPEN);

        changeOfferStatus(idOffer, OfferStatus.REJECTED);
    }

    private void changeOfferStatus(int idOffer, OfferStatus nextStatus) throws SQLException {
        Offer offer = new Offer();
        offer.setStatus(nextStatus.name());
        offerDAO.update(offer, where -> where.eq(ID_OFFER, idOffer));
    }

    public void counterOffer(int idOffer, List<Integer> idPublications) throws SQLException {
        // Controlo que la oferta y las publicaciones esten abierta
        assertStatus(idOffer, OfferStatus.OPEN);
        for (int idPublication : idPublications) {
            publicationHelper.assertStatus(idPublication, PublicationStatus.OPEN);
        }

        // Elimino las publicaciones relacionadas con la oferta
        offerDAO.deleteOfferedPublications(idOffer, where
                -> where.eq(ID_OFFER, idOffer));
        // Inserto las nuevas publicaciones
        for (int idPublication : idPublications) {
            offerDAO.addOfferedPublications(idOffer, idPublication);
        }

        // Cambio el estado a contraoferta
        Offer offer = new Offer();
        offer.setStatus(OfferStatus.CHANGED.name());
        offerDAO.update(offer, where -> where.eq(ID_OFFER, idOffer));
    }

    public Page<Offer> getOffers(int pageSize, int pageNumber, Filter filter) {
        int totalPages = offerDAO.countOffer(filter);
        return new Page(totalPages, pageNumber, pageSize, offerDAO.filterOffers(pageSize, pageNumber, filter));
    }
    
    public void assertStatus(int idOffer, OfferStatus expectedStatus) {
        OfferStatus offerStatus = offerDAO.getStatus(idOffer);
        if (!expectedStatus.equals(offerStatus)) {
            throw new IllegalStateException(String.format("Imposible ejecutar operacion en oferta [idOffer=%s] con estado %s ",
                    idOffer, offerStatus));
        }
    }

    public void assertStatusNotEqual(int idOffer, OfferStatus notExpectedStatus) {
        OfferStatus offerStatus = offerDAO.getStatus(idOffer);
        if (notExpectedStatus.equals(offerStatus)) {
            throw new IllegalStateException(String.format("Imposible ejecutar operacion en oferta [idOffer=%s] con estado %s ",
                    idOffer, offerStatus));
        }
    }
}
