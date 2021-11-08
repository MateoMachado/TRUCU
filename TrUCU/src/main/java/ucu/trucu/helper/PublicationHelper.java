package ucu.trucu.helper;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.dao.PublicationDAO;
import java.util.List;
import ucu.trucu.model.dao.ImageDAO;
import ucu.trucu.model.dao.OfferDAO;
import ucu.trucu.model.dao.ReportDAO;
import ucu.trucu.model.dto.Image;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Publication.PublicationStatus;
import ucu.trucu.model.dto.Report;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author NicoPuig
 */
@Service
public class PublicationHelper {

    @Autowired
    private PublicationDAO publicationDAO;

    @Autowired
    private ImageDAO imageDAO;

    @Autowired
    private OfferDAO offerDAO;

    @Autowired
    private ReportDAO reportDAO;

    public int createPublication(Publication newPublication) throws SQLException {
        return publicationDAO.insert(newPublication);
    }

    public void updatePublicationData(int idPublication, Publication newValues) throws SQLException {
        publicationDAO.update(newValues, where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication));
    }

    public boolean deletePublication(int idPublication) throws SQLException {
        return publicationDAO.delete(where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication)) == 1;
    }

    public Page<Publication> getPublications(int pageSize, int pageNumber, Filter filter) {
        int totalPages = publicationDAO.countPublications(filter);
        return new Page(totalPages, pageNumber, pageSize, publicationDAO.filterPublications(pageSize, pageNumber, filter));
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

    public void changePublicationStatus(int idPublication, PublicationStatus nextStatus) throws SQLException {
        Publication publication = new Publication();
        publication.setStatus(nextStatus.name());
        updatePublicationData(idPublication, publication);
    }

    public void assertStatus(int idPublication, PublicationStatus expectedStatus) {
        PublicationStatus publicationStatus = publicationDAO.getStatus(idPublication);
        if (!expectedStatus.equals(publicationStatus)) {
            throw new IllegalStateException(String.format("Imposible ejecutar operacion en publicacion [idPublication=%s] con estado %s ",
                    idPublication, publicationStatus));
        }
    }
}
