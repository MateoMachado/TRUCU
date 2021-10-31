package ucu.trucu.api;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
            offerHelper.createOffer(newOffer,publications);
            LOGGER.info("Oferta creada correctamente");
            return ResponseEntity.ok("Cuenta creada correctamente");
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
}
