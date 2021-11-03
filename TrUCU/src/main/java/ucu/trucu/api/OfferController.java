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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
    public ResponseEntity closeOffer(@RequestParam int idPublication, @RequestParam int idOffer) {
        try {
            offerHelper.closeOffer(idPublication, idOffer);
            LOGGER.info("Oferta [idOffer=%s] de publicacion [idPublication=%s] cerrada correctamente", idPublication, idOffer);
            return ResponseEntity.ok("Oferta cerrada correctamente");
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible cerrar oferta [idOffer=%s] de publicacion [idPublication=%s] -> %s",
                    idPublication, idOffer, ex);
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }

    @PostMapping("/accept")
    public ResponseEntity acceptOffer(@RequestParam int idPublication, @RequestParam int idOffer) {
        try {
            offerHelper.acceptOffer(idPublication, idOffer);
            LOGGER.info("Oferta [idOffer=%s] de publicacion [idPublication=%s] aceptada correctamente", idPublication, idOffer);
            return ResponseEntity.ok("Oferta aceptada correctamente");
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible aceptar oferta [idOffer=%s] de publicacion [idPublication=%s] -> %s",
                    idPublication, idOffer, ex);
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
