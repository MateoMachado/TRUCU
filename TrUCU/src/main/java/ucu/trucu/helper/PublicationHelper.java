package ucu.trucu.helper;

import java.sql.SQLException;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.dao.PublicationDAO;
import java.util.List;
import ucu.trucu.model.api.PublicationWrapper;
import ucu.trucu.model.dao.ImageDAO;
import ucu.trucu.model.dao.OfferDAO;
import ucu.trucu.model.dao.ReportDAO;
import ucu.trucu.model.dto.Image;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Publication.PublicationStatus;
import ucu.trucu.model.dto.Report;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author NicoPuig
 */
@Service
public class PublicationHelper {

    private static final Logger LOGGER = LoggerFactory.create(PublicationHelper.class);

    @Autowired
    private PublicationDAO publicationDAO;

    @Autowired
    private ImageDAO imageDAO;

    @Autowired
    private OfferDAO offerDAO;

    @Autowired
    private ReportDAO reportDAO;

    public int createPublication(PublicationWrapper newPublication) throws SQLException {
        int idPublication = publicationDAO.insert(newPublication.getPublication());
        imageDAO.addPublicationImages(idPublication, newPublication.getImages());
        return idPublication;
    }

    public void updatePublicationData(int idPublication, Publication newValues) throws SQLException {
        publicationDAO.update(newValues, where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication));
    }

    public boolean deletePublication(int idPublication) throws SQLException {
        return publicationDAO.delete(where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication)) == 1;
    }

    public Page<PublicationWrapper> getPublications(int pageSize, int pageNumber, Filter filter) {
        int totalPages = publicationDAO.countPublications(filter);
        List<PublicationWrapper> wrappers = new LinkedList<>();
        List<Publication> publications = publicationDAO.filterPublications(pageSize, pageNumber, filter);
        publications.forEach(publication -> wrappers.add(new PublicationWrapper(publication, imageDAO.findBy(where -> where.eq(ImageDAO.ID_PUBLICATION, publication.getIdPublication())))));
        return new Page<>(totalPages, pageNumber, pageSize, wrappers);
    }

    public List<Image> getPublicationImages(int idPublication) {
        return imageDAO.findBy(where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication));
    }

    public List<Offer> getPublicationOffers(int idPublication) {
        return offerDAO.findBy(where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication));
    }

    public List<Report> getPublicationReports(int idPublication) {
        return reportDAO.findBy(where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication));
    }

    public void closeOfferPublications(int idOffer) throws SQLException {
        publicationDAO.closeOfferPublications(idOffer);
    }

    public void cancelPublication(int idPublication) throws SQLException {
        LOGGER.info("Validando estado para cancelar publicacion [idPublication=%s]", idPublication);
        this.assertStatus(idPublication, PublicationStatus.OPEN, PublicationStatus.SETTLING, PublicationStatus.HIDDEN);

        LOGGER.info("Cancelando publicacion [idPublication=%s]", idPublication);
        changePublicationStatus(idPublication, PublicationStatus.CANCELED);

        LOGGER.info("Rechazando ofertas a publicacion [idPublication=%s]", idPublication);
        offerDAO.rejectOffersToPublication(idPublication);

        LOGGER.info("Cancelando ofertas con la publicacion [idPublication=%s]", idPublication);
        offerDAO.cancelOffersWithPublication(idPublication);
    }

    public void changePublicationStatus(int idPublication, PublicationStatus nextStatus) throws SQLException {
        Publication publication = new Publication();
        publication.setStatus(nextStatus.name());
        publicationDAO.update(publication, where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication));
    }

    public void assertStatus(int idPublication, PublicationStatus... expectedStatus) {
        PublicationStatus publicationStatus = publicationDAO.getStatus(idPublication);
        for (PublicationStatus status : expectedStatus) {
            if (status.equals(publicationStatus)) {
                return;
            }
        }
        throw new IllegalStateException(String.format("Imposible ejecutar operacion en publicacion [idPublication=%s] con estado %s",
                idPublication, publicationStatus));
    }
}
