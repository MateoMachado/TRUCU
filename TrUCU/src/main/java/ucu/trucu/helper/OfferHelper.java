package ucu.trucu.helper;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.model.dao.OfferDAO;
import ucu.trucu.model.dto.Offer;
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

        LOGGER.info("Cerrando publicacion principal [idPublication=%s]...", idPublication);
        publicationHelper.closePublication(idPublication);

        LOGGER.info("Cerrando oferta aceptada [idOffer=%s] y rechazando las otras hacia la publicacion...", idOffer);
        offerDAO.closeOfferToPublicationAndRejectOthers(idPublication, idOffer);

        LOGGER.info("Cerrando publicacion ofrecidas en la oferta [idOffer=%s]...", idOffer);
        publicationHelper.closeOfferPublications(idOffer);

        LOGGER.info("Rechazando ofertas realizadas a publicaciones de la oferta [idOffer=%s]...", idOffer);
        offerDAO.rejectOffersToPublicationsOf(idOffer);

        LOGGER.info("Cancelando otras ofertas con las publicaciones de la oferta [idOffer=%s]...", idOffer);
        offerDAO.cancelOtherOffersWithPublicationsOf(idOffer);
    }
}
