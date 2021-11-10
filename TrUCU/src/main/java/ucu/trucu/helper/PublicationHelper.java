package ucu.trucu.helper;

import java.sql.SQLException;
import java.util.LinkedList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.dao.PublicationDAO;
import java.util.List;
import java.util.stream.Collectors;
import ucu.trucu.model.api.PublicationWrapper;
import ucu.trucu.model.dao.ImageDAO;
import ucu.trucu.model.dao.OfferDAO;
import ucu.trucu.model.dao.ReportDAO;
import ucu.trucu.model.dto.Image;
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
public class PublicationHelper implements EntityHelper<Integer, PublicationWrapper> {

    private static final Logger LOGGER = LoggerFactory.create(PublicationHelper.class);

    @Autowired
    private PublicationDAO publicationDAO;

    @Autowired
    private ImageDAO imageDAO;

    @Autowired
    private OfferDAO offerDAO;

    @Autowired
    private ReportDAO reportDAO;

    @Override
    public int create(PublicationWrapper newPublication) throws SQLException {
        LOGGER.info("Creando nueva publicacion...");

        int idPublication = publicationDAO.insert(newPublication.getPublication());

        LOGGER.info("Insertando imagenes a nueva publicacion [idPublication=%s]...", idPublication);
        imageDAO.addPublicationImages(idPublication, newPublication.getImages());
        return idPublication;
    }

    @Override
    public int update(Integer idPublication, PublicationWrapper newValues) throws SQLException {
        return publicationDAO.update(newValues.getPublication(), where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication));
    }

    @Override
    public boolean delete(Integer idPublication) throws SQLException {
        LOGGER.info("Eliminando publicacion [idPublication=%s] y sus imagenes...", idPublication);
        imageDAO.delete(where -> where.eq(ImageDAO.ID_PUBLICATION, idPublication));
        return publicationDAO.delete(where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication)) == 1;
    }

    @Override
    public Page<PublicationWrapper> filter(int pageSize, int pageNumber, Filter filter) {

        int totalPages = publicationDAO.count(filter);
        List<Publication> publications = publicationDAO.filterPublications(pageSize, pageNumber, filter);
        List<Integer> publicationIds = new LinkedList<>();
        publications.forEach(publication -> publicationIds.add(publication.getIdPublication()));
        List<Image> images = imageDAO.findBy(where -> where.in(ImageDAO.ID_PUBLICATION, publicationIds));
        List<PublicationWrapper> wrappers = new LinkedList<>();
        publications.forEach(publication -> {
            wrappers.add(new PublicationWrapper(
                    publication,
                    images.stream()
                            .filter(image -> image.getIdPublication().equals(publication.getIdPublication()))
                            .collect(Collectors.toList()))
            );
        });

        return new Page<>(totalPages, pageNumber, pageSize, wrappers);
    }

    public List<Image> getPublicationImages(int idPublication) {
        return imageDAO.findBy(where -> where.eq(PublicationDAO.ID_PUBLICATION, idPublication));
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
