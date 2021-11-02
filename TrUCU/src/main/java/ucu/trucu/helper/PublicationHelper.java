package ucu.trucu.helper;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.dao.PublicationDAO;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
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

/**
 *
 * @author NicoPuig
 */
@Service
public class PublicationHelper {

    private static final Logger LOGGER = LoggerFactory.create(PublicationHelper.class);
    private static final String ID_PUBLICATION = "idPublication";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String UCUCOIN_VALUE = "ucuCoinValue";
    private static final String PUBLICATION_DATE = "publicationDate";
    private static final String STATUS = "status";
    private static final String ACCOUNT_CI = "accountCI";

    @Autowired
    private PublicationDAO publicationDAO;

    @Autowired
    private ImageDAO imageDAO;

    @Autowired
    private OfferDAO offerDAO;

    @Autowired
    private ReportDAO reportDAO;

    @Autowired
    private OfferHelper offerHelper;

    public Filter buildPublicationFilter(Integer idPublication, String title, String description, Integer maxUcuCoins,
            Integer minUcuCoins, Timestamp afterDate, Timestamp beforeDate, String status, String accountCI) {
        return Filter.build(where -> {
            List<String> conditions = new LinkedList<>();
            if (idPublication != null) {
                conditions.add(where.eq(ID_PUBLICATION, idPublication));
            }
            if (title != null) {
                conditions.add(where.eq(TITLE, title));
            }
            if (description != null) {
                conditions.add(where.eq(DESCRIPTION, description));
            }
            if (maxUcuCoins != null) {
                conditions.add(where.loet(UCUCOIN_VALUE, maxUcuCoins));
            }
            if (minUcuCoins != null) {
                conditions.add(where.goet(UCUCOIN_VALUE, minUcuCoins));
            }
            if (afterDate != null) {
                conditions.add(where.goet(PUBLICATION_DATE, afterDate));
            }
            if (beforeDate != null) {
                conditions.add(where.loet(PUBLICATION_DATE, beforeDate));
            }
            if (status != null) {
                conditions.add(where.eq(STATUS, status));
            }
            if (accountCI != null) {
                conditions.add(where.eq(ACCOUNT_CI, accountCI));
            }
            return conditions.isEmpty() ? null : where.and(conditions.toArray(new String[0]));
        });
    }

    public int createPublication(Publication newPublication) throws SQLException {
        return publicationDAO.insert(newPublication);
    }

    public void updatePublicationData(int idPublication, Publication newValues) throws SQLException {
        publicationDAO.update(newValues, where -> where.eq(ID_PUBLICATION, idPublication));
    }

    public boolean deletePublication(int idPublication) throws SQLException {
        return publicationDAO.delete(where -> where.eq(ID_PUBLICATION, idPublication)) == 1;
    }

    public List<Publication> getPublications(int pageSize, int pageNumber, Filter filter) {
        return publicationDAO.filterPublications(pageSize, pageNumber, filter);
    }

    public List<Image> getPublicationImages(int idPublication) {
        return imageDAO.findBy(where -> where.eq(ID_PUBLICATION, idPublication));
    }

    public List<Offer> getPublicationOffers(int idPublication) {
        return offerDAO.findBy(where -> where.eq(ID_PUBLICATION, idPublication));
    }

    public List<Report> getPublicationReports(int idPublication) {
        return reportDAO.findBy(where -> where.eq(ID_PUBLICATION, idPublication));
    }

    public PublicationStatus getStatus(int idPublication) {
        return PublicationStatus.valueOf(publicationDAO.findByPK(idPublication).getStatus());
    }

    public void closePublication(int idPublication) throws SQLException, IllegalStateException {
        PublicationStatus publicationStatus = getStatus(idPublication);
        if (PublicationStatus.OPEN.equals(publicationStatus)) {
            Publication publication = new Publication();
            publication.setStatus(PublicationStatus.CLOSED.name());
            publicationDAO.update(publication, where -> where.eq(ID_PUBLICATION, idPublication));
        } else {
            throw new IllegalStateException(String.format("Publicacion no disponible para cerrarse [idPublication=%s, Status=%s]",
                    idPublication, publicationStatus));
        }
    }

    public void finishPublicationAndOffer(int idPublication, int idOffer) throws SQLException {

        LOGGER.info("Cerrando publicacion principal [idPublication=%s]...", idPublication);
        closePublication(idPublication);

        LOGGER.info("Cerrando oferta aceptada [idOffer=%s] y rechazando las otras hacia la publicacion...", idOffer);
        offerHelper.closeAcceptedOffer(idPublication, idOffer);

        LOGGER.info("Cerrando publicacion ofrecidas en la oferta [idOffer=%s]...", idOffer);
        publicationDAO.closeOfferPublications(idOffer);

        LOGGER.info("Rechazando ofertas realizadas a publicaciones de la oferta [idOffer=%s]...", idOffer);
        offerHelper.rejectOffersToPublicationsOf(idOffer);

        LOGGER.info("Cancelando otras ofertas con las publicaciones de la oferta [idOffer=%s]...", idOffer);
        offerHelper.cancelOtherOffersWithPublicationsOf(idOffer);
    }
}
