package ucu.trucu.api;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ucu.trucu.database.DBController;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.helper.OfferHelper;
import ucu.trucu.model.api.OfferWrapper;
import ucu.trucu.model.filter.OfferFilter;
import ucu.trucu.util.api.Message;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author Seba Mazzey
 */
@RestController
@RequestMapping("trucu/offer")
@CrossOrigin(origins = {"${trucu.front.url.local}", "${trucu.front.url.firebase}"})
public class OfferController {

    private static final Logger LOGGER = LoggerFactory.create(PublicationController.class);

    @Autowired
    private OfferHelper offerHelper;

    @Autowired
    private DBController dbController;

    @PostMapping("/create")
    public ResponseEntity createOffer(@RequestParam int idPublication, @RequestBody List<Integer> idOfferedPublications) {
        try {
            offerHelper.createOffer(idPublication, idOfferedPublications);
            dbController.commit();
            LOGGER.info("Oferta creada correctamente");
            return ResponseEntity.ok(new Message("Oferta creada correctamente"));
        } catch (SQLException ex) {
            LOGGER.error("Imposible crear la oferta -> %s", ex.getMessage());
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteOffer(@RequestParam int idOffer) {
        try {
            offerHelper.deleteOffer(idOffer);
            dbController.commit();
            return ResponseEntity.ok(new Message("Oferta eliminada correctamente"));
        } catch (SQLException ex) {
            LOGGER.error("Imposible eliminar la oferta [idOffer=%d] -> %s", idOffer, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<OfferWrapper>> getOffers(
            OfferFilter offerFilter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "0") int pageSize) {

        Filter filter = offerFilter.toFilter();
        LOGGER.info("Obteniendo ofertas filtradas por [%s]", filter);
        return ResponseEntity.ok(offerHelper.filter(pageSize, pageNumber, filter));
    }

    @PostMapping("/close")
    public ResponseEntity closeOffer(@RequestParam int idOffer) {
        try {
            offerHelper.closeOffer(idOffer);
            dbController.commit();
            LOGGER.info("Oferta [idOffer=%s] cerrada correctamente", idOffer);
            return ResponseEntity.ok(new Message("Oferta cerrada correctamente"));
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible cerrar oferta [idOffer=%s] -> %s", idOffer, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/accept")
    public ResponseEntity acceptOffer(@RequestParam int idOffer) {
        try {
            offerHelper.acceptOffer(idOffer);
            dbController.commit();
            LOGGER.info("Oferta [idOffer=%s] aceptada correctamente", idOffer);
            return ResponseEntity.ok(new Message("Oferta aceptada correctamente"));
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible aceptar oferta [idOffer=%s] -> %s", idOffer, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity cancelOffer(@RequestParam int idOffer) {
        try {
            offerHelper.cancelOffer(idOffer);
            dbController.commit();
            LOGGER.info("Oferta [idOffer=%s] cancelada correctamente", idOffer);
            return ResponseEntity.ok(new Message("Oferta cancelada correctamente"));
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible cancelar oferta [idOffer=%s] -> %s", idOffer, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/revert")
    public ResponseEntity revertAcceptance(@RequestParam int idOffer) {
        try {
            offerHelper.revertAcceptance(idOffer);
            dbController.commit();
            LOGGER.info("Oferta [idOffer=%s] desaceptada correctamente", idOffer);
            return ResponseEntity.ok(new Message("Oferta desaceptada correctamente"));
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible desaceptada oferta [idOffer=%s]  -> %s", idOffer, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/reject")
    public ResponseEntity rejectOffer(@RequestParam int idOffer) {
        try {
            offerHelper.rejectOffer(idOffer);
            dbController.commit();
            LOGGER.info("Oferta [idOffer=%s] rechazada correctamente", idOffer);
            return ResponseEntity.ok(new Message("Oferta rechazada correctamente"));
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Error al rechazar la oferta [idOffer=%s] -> %s", idOffer, ex.getMessage());
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/counterOffer")
    public ResponseEntity counterOffer(@RequestParam int idOffer, @RequestBody List<Integer> publications) {
        try {
            offerHelper.counterOffer(idOffer, publications);
            dbController.commit();
            LOGGER.info("Se creo la contraoferta [idOffer=%s] correctamente", idOffer);
            return ResponseEntity.ok(new Message("Contraoferta realizada correctamente"));
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Error al crear la contraoferta [idOffer=%s] -> %s", idOffer, ex.getMessage());
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getMessage()));
        }
    }

    @PostMapping("/counterOffer/accept")
    public ResponseEntity acceptCounterOffer(@RequestParam int idOffer) {
        try {
            offerHelper.acceptCounterOffer(idOffer);
            dbController.commit();
            LOGGER.info("Contraoferta [idOffer=%s] aceptada correctamente", idOffer);
            return ResponseEntity.ok(new Message("Contraoferta aceptada correctamente"));
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Error al aceptar contraoferta [idOffer=%s] -> %s", idOffer, ex.getMessage());
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getMessage()));
        }
    }

    @PostMapping("/counterOffer/reject")
    public ResponseEntity rejectCounterOffer(@RequestParam int idOffer) {
        try {
            offerHelper.rejectCounterOffer(idOffer);
            dbController.commit();
            LOGGER.info("Contraoferta [idOffer=%s] rechazada correctamente", idOffer);
            return ResponseEntity.ok(new Message("Contraoferta rechazada correctamente"));
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Error al rechazar contraoferta [idOffer=%s] -> %s", idOffer, ex.getMessage());
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getMessage()));
        }
    }
}
