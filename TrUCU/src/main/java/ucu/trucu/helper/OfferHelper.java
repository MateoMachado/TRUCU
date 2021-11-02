package ucu.trucu.helper;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.helper.validation.OfferValidator;
import ucu.trucu.helper.validation.PublicationValidator;
import ucu.trucu.model.dao.OfferDAO;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Offer.OfferStatus;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Publication.PublicationStatus;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

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
    private OfferValidator offerValidator;
    
    @Autowired
    private PublicationValidator publicationValidator;

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

    public void closeOffer(int idPublication, int idOffer) throws SQLException {

        LOGGER.info("Validando estados para cerrar oferta [idPublication=%s, idOffer=%s]", idPublication, idOffer);
        publicationValidator.assertStatus(idPublication, PublicationStatus.SETTLING);
        offerValidator.assertStatus(idOffer, OfferStatus.SETTLING);

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

    public void acceptOffer(int idPublication, int idOffer) throws SQLException {

        LOGGER.info("Validando estados para aceptar oferta [idPublication=%s, idOffer=%s]", idPublication, idOffer);
        publicationValidator.assertStatus(idPublication, PublicationStatus.OPEN);
        offerValidator.assertStatus(idOffer, OfferStatus.OPEN);

        LOGGER.info("Aceptando oferta [idOffer=%s] en publicacion [idPublication=%s]...", idOffer, idPublication);
        publicationHelper.changePublicationStatus(idPublication, PublicationStatus.SETTLING);

        LOGGER.info("Aceptando oferta [idOffer=%s]...", idOffer, idPublication);
        changeOfferStatus(idOffer, OfferStatus.SETTLING);
    }

    private void changeOfferStatus(int idOffer, OfferStatus nextStatus) throws SQLException {
        Offer offer = new Offer();
        offer.setStatus(nextStatus.name());
        offerDAO.update(offer, where -> where.eq(ID_OFFER, idOffer));
    }
    
    public void counterOffer(int idOffer, List<Integer> idPublications) throws SQLException {
        // Controlo que la oferta y las publicaciones esten abierta
        offerValidator.assertStatus(idOffer, OfferStatus.OPEN);
        for (int idPublication : idPublications) {
            publicationValidator.assertStatus(idPublication, PublicationStatus.OPEN);
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
}
