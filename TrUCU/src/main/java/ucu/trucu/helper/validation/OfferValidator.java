package ucu.trucu.helper.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.model.dao.OfferDAO;
import ucu.trucu.model.dto.Offer.OfferStatus;

/**
 *
 * @author NicoPuig
 */
@Service
public class OfferValidator {

    @Autowired
    private OfferDAO offerDAO;

    public void assertStatus(int idOffer, OfferStatus expectedStatus) {
        OfferStatus offerStatus = offerDAO.getStatus(idOffer);
        if (!expectedStatus.equals(offerStatus)) {
            throw new IllegalStateException(String.format("Imposible ejecutar operacion en oferta [idOffer=%s] con estado %s ",
                    idOffer, offerStatus));
        }
    }
}
