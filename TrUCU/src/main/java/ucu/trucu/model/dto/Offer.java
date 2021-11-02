package ucu.trucu.model.dto;

import java.sql.Date;

/**
 *
 * @author NicoPuig
 */
public class Offer {
    
    public enum OfferStatus{
        OPEN, CLOSED, SETTLING, REJECTED, CANCELED
    }

    private Integer idOffer;
    private String status;
    private Date offerDate;
    private Integer idPublication;

    public Integer getIdOffer() {
        return idOffer;
    }

    public void setIdOffer(Integer idOffer) {
        this.idOffer = idOffer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Date offerDate) {
        this.offerDate = offerDate;
    }

    public Integer getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(Integer idPublication) {
        this.idPublication = idPublication;
    }
}
