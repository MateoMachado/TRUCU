package ucu.trucu.model.dao;

import org.springframework.stereotype.Component;
import ucu.trucu.model.dto.Offer;

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
}
