package ucu.trucu.model.api;

import java.util.List;
import ucu.trucu.model.dto.Image;
import ucu.trucu.model.dto.Publication;

/**
 *
 * @author NicoPuig
 */
public class PublicationWrapper {

    private Publication publication;
    private List<Image> images;

    public PublicationWrapper(Publication publication, List<Image> images) {
        this.publication = publication;
        this.images = images;
    }

    public Publication getPublication() {
        return publication;
    }

    public void setPublication(Publication publication) {
        this.publication = publication;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
