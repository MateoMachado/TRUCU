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
    @Autowired
    private OfferDAO offerDAO;
    
    public void createOffer(Offer newOffer, List<Integer> idPublications) throws SQLException {
        offerDAO.insert(newOffer);

        for(int idPublication: idPublications) {
            offerDAO.addOfferedPublications(newOffer, idPublication);
        }
    }
    
    public void deleteOffer(int idOffer) throws SQLException {
        // Elimino las publicaciones relacionadas con la oferta
        offerDAO.deleteOfferedPublications(idOffer, where -> where.and(
                where.eq("idOffer", idOffer))
        );
        
        // Elimino la oferta
        offerDAO.delete(where -> where.and(
                where.eq("idOffer", idOffer))
        );
    }
}
