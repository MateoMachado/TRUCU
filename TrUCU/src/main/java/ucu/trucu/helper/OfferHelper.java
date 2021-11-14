package ucu.trucu.helper;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.api.OfferWrapper;
import ucu.trucu.model.dao.OfferDAO;
import ucu.trucu.model.dao.PublicationDAO;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.model.dto.Offer.OfferStatus;
import ucu.trucu.model.dto.Publication.PublicationStatus;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author NicoPuig
 */
@Service
public class OfferHelper {
    
    private static final Logger LOGGER = LoggerFactory.create(OfferHelper.class);
    
    @Autowired
    private OfferDAO offerDAO;
    
    @Autowired
    private PublicationDAO publicationDAO;
    
    @Autowired
    private PublicationHelper publicationHelper;
    
    public void createOffer(int idPublication, List<Integer> idOfferedPublications) throws SQLException {
        Offer offer = new Offer();
        offer.setIdPublication(idPublication);
        int idOffer = offerDAO.insert(offer);
        
        offerDAO.addOfferedPublications(idOffer, idOfferedPublications);
    }
    
    public void deleteOffer(int idOffer) throws SQLException {
        // Elimino las publicaciones relacionadas con la oferta
        offerDAO.deleteOfferedPublications(where -> where.eq(OfferDAO.ID_OFFER, idOffer));

        // Elimino la oferta
        offerDAO.delete(where -> where.eq(OfferDAO.ID_OFFER, idOffer));
    }
    
    public void closeOffer(int idOffer) throws SQLException {
        
        LOGGER.info("Validando estados para cerrar oferta [idOffer=%s]", idOffer);
        this.assertStatus(idOffer, OfferStatus.SETTLING);
        int idPublication = offerDAO.getOfferPublicationId(idOffer);
        publicationHelper.assertStatus(idPublication, PublicationStatus.SETTLING);
        
        LOGGER.info("Cerrando publicacion principal [idPublication=%s]...", idPublication);
        publicationHelper.changePublicationStatus(idPublication, PublicationStatus.CLOSED);
        
        LOGGER.info("Cerrando oferta aceptada [idOffer=%s], y rechazando todas las otras ofertas a la publicacion [idPublication=%s]...", idOffer, idPublication);
        offerDAO.closeOfferToPublicationAndRejectOthers(idPublication, idOffer);
        
        LOGGER.info("Cerrando publicacion ofrecidas en la oferta [idOffer=%s]...", idOffer);
        publicationDAO.closeOfferPublications(idOffer);
        
        LOGGER.info("Rechazando ofertas realizadas a publicaciones de la oferta [idOffer=%s]...", idOffer);
        offerDAO.rejectOffersToPublicationsOf(idOffer);
        
        LOGGER.info("Deshaciendo negociaciones con la publicacion cerrada [idPublication=%s]...", idPublication);
        publicationDAO.reopenSettlingPublicationWithCanceledOffer(idPublication);
        
        LOGGER.info("Cancelando ofertas con las publicaciones de la oferta [idOffer=%s]...", idOffer);
        offerDAO.cancelOffersWithPublicationsOf(idPublication, idOffer);
    }
    
    public void acceptOffer(int idOffer) throws SQLException {
        
        LOGGER.info("Validando estados para aceptar oferta [idOffer=%s]", idOffer);
        this.assertStatus(idOffer, OfferStatus.OPEN);
        int idPublication = offerDAO.getOfferPublicationId(idOffer);
        publicationHelper.assertStatus(idPublication, PublicationStatus.OPEN);
        
        LOGGER.info("Aceptando oferta [idOffer=%s] en publicacion [idPublication=%s]...", idOffer, idPublication);
        publicationHelper.changePublicationStatus(idPublication, PublicationStatus.SETTLING);
        
        LOGGER.info("Aceptando oferta [idOffer=%s]...", idOffer, idPublication);
        changeOfferStatus(idOffer, OfferStatus.SETTLING);
    }
    
    public void cancelOffer(int idOffer) throws SQLException {
        
        LOGGER.info("Validando estados para cancelar oferta [idOffer=%s]", idOffer);
        this.assertStatusNotEqual(idOffer, OfferStatus.CLOSED);
        
        OfferStatus offerStatus = offerDAO.getStatus(idOffer);
        if (OfferStatus.SETTLING.equals(offerStatus)) {
            int idPublication = offerDAO.getOfferPublicationId(idOffer);
            publicationHelper.changePublicationStatus(idPublication, PublicationStatus.OPEN);
        }
        changeOfferStatus(idOffer, OfferStatus.CANCELED);
    }
    
    public void revertAcceptance(int idOffer) throws SQLException {
        
        LOGGER.info("Validando estados para revertir aceptacion de oferta [idOffer=%s]", idOffer);
        this.assertStatus(idOffer, OfferStatus.SETTLING);
        int idPublication = offerDAO.getOfferPublicationId(idOffer);
        publicationHelper.assertStatus(idPublication, PublicationStatus.SETTLING);
        
        publicationHelper.changePublicationStatus(idPublication, PublicationStatus.OPEN);
        changeOfferStatus(idOffer, OfferStatus.OPEN);
    }
    
    public void rejectOffer(int idOffer) throws SQLException {
        
        LOGGER.info("Validando estados para rechazar oferta [idOffer=%s]", idOffer);
        assertStatus(idOffer, OfferStatus.OPEN, OfferStatus.CHANGED);
        
        changeOfferStatus(idOffer, OfferStatus.REJECTED);
    }
    
    public void counterOffer(int idOffer, List<Integer> idPublications) throws SQLException {
        // Controlo que la oferta y las publicaciones esten abierta
        LOGGER.info("Validando estados para contraofertar [idOffer=%s]...", idOffer);
        assertStatus(idOffer, OfferStatus.OPEN);
        publicationHelper.assertStatus(idPublications, PublicationStatus.OPEN);
        
        LOGGER.info("Actualizando publicaciones ofertadas [idOffer=%s]...", idOffer);

        // Elimino las publicaciones relacionadas con la oferta
        offerDAO.deleteOfferedPublications(where -> where.eq(OfferDAO.ID_OFFER, idOffer));

        // Inserto las nuevas publicaciones
        offerDAO.addOfferedPublications(idOffer, idPublications);

        // Cambio el estado a contraoferta
        changeOfferStatus(idOffer, OfferStatus.CHANGED);
    }
    
    public void acceptCounterOffer(int idOffer) throws SQLException {
        
        LOGGER.info("Validando estados para aceptar contraoferta [idOffer=%s]...", idOffer);
        assertStatus(idOffer, OfferStatus.CHANGED);
        
        LOGGER.info("Aceptando contraoferta [idOffer=%s]...", idOffer);
        changeOfferStatus(idOffer, OfferStatus.OPEN);
    }
    
    public void rejectCounterOffer(int idOffer) throws SQLException {
        
        LOGGER.info("Validando estados para rechazar contraoferta [idOffer=%s]...", idOffer);
        assertStatus(idOffer, OfferStatus.CHANGED);
        
        LOGGER.info("Aceptando contraoferta [idOffer=%s]...", idOffer);
        changeOfferStatus(idOffer, OfferStatus.REJECTED);
    }
    
    public Page<OfferWrapper> filter(int pageSize, int pageNumber, Filter filter) {
        
        int totalPages = offerDAO.count(filter);
        List<Offer> offers = offerDAO.filterOffers(pageSize, pageNumber, filter);
        List<OfferWrapper> wrappers = new LinkedList<>();
        offers.forEach(offer -> wrappers.add(new OfferWrapper(
                offer,
                publicationDAO.getOfferedPublications(offer.getIdOffer()),
                publicationDAO.findByPK(offer.getIdPublication())
        )));
        return new Page(totalPages, pageNumber, pageSize, wrappers);
    }
    
    private void changeOfferStatus(int idOffer, OfferStatus nextStatus) throws SQLException {
        Offer offer = new Offer();
        offer.setStatus(nextStatus.name());
        offerDAO.update(offer, where -> where.eq(OfferDAO.ID_OFFER, idOffer));
    }
    
    public void assertStatus(int idOffer, OfferStatus... expectedStatus) {
        OfferStatus offerStatus = offerDAO.getStatus(idOffer);
        for (OfferStatus status : expectedStatus) {
            if (status.equals(offerStatus)) {
                return;
            }
        }
        throw new IllegalStateException(String.format("Imposible ejecutar operacion en oferta [idOffer=%s] con estado %s ",
                idOffer, offerStatus));
    }
    
    public void assertStatusNotEqual(int idOffer, OfferStatus notExpectedStatus) {
        OfferStatus offerStatus = offerDAO.getStatus(idOffer);
        if (notExpectedStatus.equals(offerStatus)) {
            throw new IllegalStateException(String.format("Imposible ejecutar operacion en oferta [idOffer=%s] con estado %s ",
                    idOffer, offerStatus));
        }
    }
}
