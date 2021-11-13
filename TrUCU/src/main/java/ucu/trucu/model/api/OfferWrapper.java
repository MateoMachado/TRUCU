package ucu.trucu.model.api;

import java.util.List;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Publication;

/**
 *
 * @author NicoPuig
 */
public class OfferWrapper {

    private Offer offer;
    private List<Publication> offeredPublications;
    private Publication publication;

    public OfferWrapper(Offer offer, List<Publication> offeredPublications, Publication publication) {
        this.offer = offer;
        this.offeredPublications = offeredPublications;
        this.publication = publication;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public List<Publication> getOfferedPublications() {
        return offeredPublications;
    }

    public void setOfferedPublications(List<Publication> offeredPublications) {
        this.offeredPublications = offeredPublications;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }
}
