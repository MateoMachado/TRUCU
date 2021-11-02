package ucu.trucu.helper;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.model.dao.OfferDAO;
import ucu.trucu.model.dto.Offer;

/**
 *
 * @author NicoPuig
 */
@Service
public class OfferHelper {

    private static final String ID_OFFER = "idOffer";

    @Autowired
    private OfferDAO offerDAO;

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

    public void closeAcceptedOffer(int closedPublicationId, int acceptedOfferId) throws SQLException {
        offerDAO.closeOfferToPublicationAndRejectOthers(closedPublicationId, acceptedOfferId);
    }
    
    public void rejectOffersToPublicationsOf(int idOffer) throws SQLException{
        offerDAO.rejectOffersToPublicationsOf(idOffer);
    }

    public void cancelOtherOffersWithPublicationsOf(int idOffer) throws SQLException {
        offerDAO.cancelOtherOffersWithPublicationsOf(idOffer);
    }
}
