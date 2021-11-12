package ucu.trucu.model.filter;

import ucu.trucu.database.querybuilder.Filter;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import ucu.trucu.model.dao.OfferDAO;

/**
 *
 * @author NicoPuig
 */
public class OfferFilter implements DTOFilter {

    private Integer idOffer;
    private String[] status;
    private Timestamp afterOfferDate;
    private Timestamp beforeOfferDate;
    private Integer idPublication;

    public Integer getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(Integer idOffer) {
        this.idOffer = idOffer;
    }

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public Timestamp getAfterOfferDate() {
        return afterOfferDate;
    }

    public void setAfterOfferDate(Timestamp afterOfferDate) {
        this.afterOfferDate = afterOfferDate;
    }

    public Timestamp getBeforeOfferDate() {
        return beforeOfferDate;
    }

    public void setBeforeOfferDate(Timestamp beforeOfferDate) {
        this.beforeOfferDate = beforeOfferDate;
    }

    public Integer getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(Integer idPublication) {
        this.idPublication = idPublication;
    }

    @Override
    public Filter toFilter() {
        return Filter.build(where -> {
            List<String> conditions = new LinkedList<>();
            if (idOffer != null) {
                conditions.add(where.eq(OfferDAO.ID_OFFER, idOffer));
            }
            if (status != null) {
                conditions.add(where.in(OfferDAO.STATUS, status));
            }
            if (idPublication != null) {
                conditions.add(where.eq(OfferDAO.ID_PUBLICATION, idPublication));
            }
            if (afterOfferDate != null) {
                conditions.add(where.goet(OfferDAO.OFFER_DATE, afterOfferDate));
            }
            if (beforeOfferDate != null) {
                conditions.add(where.loet(OfferDAO.OFFER_DATE, beforeOfferDate));
            }
            return conditions.isEmpty() ? null : where.and(conditions.toArray(new String[0]));
        });
    }
}
