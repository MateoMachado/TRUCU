package ucu.trucu.api;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ucu.trucu.helper.OfferHelper;
import ucu.trucu.model.dto.Offer;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author Seba Mazzey
 */
@RestController
@RequestMapping("trucu/offer")
public class OfferController {

    private static final Logger LOGGER = LoggerFactory.create(PublicationController.class);

    @Autowired
    private OfferHelper offerHelper;

    @PostMapping("/create")
    public ResponseEntity createOffer(@RequestBody Offer newOffer, @RequestParam List<Integer> publications) {
        try {
            offerHelper.createOffer(newOffer, publications);
            LOGGER.info("Oferta creada correctamente");
            return ResponseEntity.ok("Oferta creada correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Imposible crear la oferta -> %s", ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteOffer(@RequestParam int idOffer) {
        try {
            offerHelper.deleteOffer(idOffer);
            return ResponseEntity.ok("Oferta eliminada correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Imposible eliminar la oferta [idOffer=%d] -> %s", idOffer, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @GetMapping("/getFromUser")
    public ResponseEntity<List<Offer>> getUserOffers(@RequestParam int idUser) {
        return ResponseEntity.ok(offerHelper.getUserOffers(idUser));
    }

    @PostMapping("/close")
    public ResponseEntity closeOffer(@RequestParam int idOffer) {
        try {
            offerHelper.closeOffer(idOffer);
            LOGGER.info("Oferta [idOffer=%s] cerrada correctamente", idOffer);
            return ResponseEntity.ok("Oferta cerrada correctamente");
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible cerrar oferta [idOffer=%s] -> %s", idOffer, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @PostMapping("/accept")
    public ResponseEntity acceptOffer(@RequestParam int idOffer) {
        try {
            offerHelper.acceptOffer(idOffer);
            LOGGER.info("Oferta [idOffer=%s] aceptada correctamente", idOffer);
            return ResponseEntity.ok("Oferta aceptada correctamente");
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible aceptar oferta [idOffer=%s] -> %s", idOffer, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity cancelOffer(@RequestParam int idOffer) {
        try {
            offerHelper.cancelOffer(idOffer);
            LOGGER.info("Oferta [idOffer=%s] cancelada correctamente", idOffer);
            return ResponseEntity.ok("Oferta cancelada correctamente");
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible cancelar oferta [idOffer=%s] -> %s", idOffer, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @PostMapping("/revert")
    public ResponseEntity revertAcceptance(@RequestParam int idOffer) {
        try {
            offerHelper.revertAcceptance(idOffer);
            LOGGER.info("Oferta [idOffer=%s] desaceptada correctamente", idOffer);
            return ResponseEntity.ok("Oferta desaceptada correctamente");
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible desaceptada oferta [idOffer=%s]  -> %s", idOffer, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }
    
    @PostMapping("/counterOffer")
    public ResponseEntity counterOffer(@RequestParam int idOffer, @RequestParam List<Integer> publications) {
        try {
            offerHelper.counterOffer(idOffer, publications);
            LOGGER.info("Se creo la contraoferta [idOffer=%s] correctamente",idOffer);
            return ResponseEntity.ok("Contraoferta realizada correctamente");
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Error al crear la contraoferta [idOffer=%s] -> %s",idOffer,ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }
}
