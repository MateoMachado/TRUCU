package ucu.trucu.model.dto;

import java.sql.Timestamp;

/**
 *
 * @author NicoPuig
 */
public class Offer {
    
    public enum OfferStatus{
        OPEN, CLOSED, SETTLING, REJECTED, CANCELED, CHANGED
    }

    private Integer idOffer;
    private String status;
    private Timestamp offerDate;
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

    public Timestamp getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(Timestamp offerDate) {
        this.offerDate = offerDate;
    }

    public Integer getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(Integer idPublication) {
        this.idPublication = idPublication;
    }
}
