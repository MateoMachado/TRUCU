package ucu.trucu.api;

import java.sql.SQLException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ucu.trucu.helper.PublicationHelper;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import ucu.trucu.database.DBController;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.api.PublicationWrapper;
import ucu.trucu.model.filter.PublicationFilter;
import ucu.trucu.util.api.Message;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author NicoPuig
 */
@RestController
@RequestMapping("trucu/publication")
@CrossOrigin(origins = {"${trucu.front.url.local}", "${trucu.front.url.firebase}"})
public class PublicationController {

    private static final Logger LOGGER = LoggerFactory.create(PublicationController.class);

    @Autowired
    private PublicationHelper publicationHelper;

    @Autowired
    private DBController dbController;

    @PostMapping("/create")
    public ResponseEntity createPublication(@RequestBody PublicationWrapper newPublication) {
        try {
            int idPublication = publicationHelper.create(newPublication);
            dbController.commit();
            LOGGER.info("Publicacion [idPublication=%s] creada correctamente", idPublication);
            return ResponseEntity.ok(idPublication);
        } catch (SQLException ex) {
            LOGGER.error("Imposible crear publicacion -> %s", ex.getMessage());
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity updatePublication(@RequestParam int idPublication, @RequestBody PublicationWrapper newValues) {
        try {
            publicationHelper.update(idPublication, newValues);
            dbController.commit();
            LOGGER.info("Valores actualizados en publicacion [idPublication=%s]", idPublication);
            return ResponseEntity.ok(new Message("Valores de actualizados correctamente"));
        } catch (SQLException ex) {
            LOGGER.error("Imposible actualizar valores para publicacion [idPublication=%s] -> %s", idPublication, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity cancelPublication(@RequestParam int idPublication) {
        try {
            publicationHelper.cancelPublication(idPublication);
            dbController.commit();
            LOGGER.info("Valores actualizados en publicacion [idPublication=%s]", idPublication);
            return ResponseEntity.ok(new Message("Publicacion cancelada correctamente"));
        } catch (SQLException | IllegalStateException ex) {
            LOGGER.error("Imposible actualizar valores para publicacion [idPublication=%s] -> %s", idPublication, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity deleteAccount(@RequestParam int idPublication) {
        try {
            if (publicationHelper.delete(idPublication)) {
                dbController.commit();
                LOGGER.info("Publicacion [idPublication=%s] eliminada correctamente", idPublication);
                return ResponseEntity.ok(new Message("Publicacion eliminada correctamente"));
            } else {
                LOGGER.warn("Publicacion [idPublication=%s] no existente", idPublication);
                return ResponseEntity.ok(new Message("Publicacion no exsitente"));
            }
        } catch (SQLException ex) {
            LOGGER.error("Imposible eliminar publicacion [idPublication=%s] -> %s", idPublication, ex);
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<Page<PublicationWrapper>> getPublications(
            PublicationFilter publicationFilter,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "0") int pageSize) {

        Filter filter = publicationFilter.toFilter();
        LOGGER.info("Obteniendo publicaciones filtradas por [%s]", filter);
        return ResponseEntity.ok(publicationHelper.filter(pageSize, pageNumber, filter));
    }

    @PostMapping("/hide")
    public ResponseEntity hidePublication(@RequestParam int idPublication) {
        try {
            publicationHelper.hidePublication(idPublication);
            dbController.commit();
            LOGGER.info("Publicacion [idPublication=%s] ocultada correctamente");
            return ResponseEntity.ok(new Message("Publicacion ocultada correctamente"));
        } catch (SQLException ex) {
            dbController.rollback();
            LOGGER.error("Imposible ocultar publicacion [idPublication=%s] -> %s", idPublication, ex);
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/show")
    public ResponseEntity showPublication(@RequestParam int idPublication) {
        try {
            publicationHelper.showPublication(idPublication);
            dbController.commit();
            LOGGER.info("Publicacion [idPublication=%s] descocultada correctamente");
            return ResponseEntity.ok(new Message("Publicacion descocultada correctamente"));
        } catch (SQLException ex) {
            dbController.rollback();
            LOGGER.error("Imposible descocultada publicacion [idPublication=%s] -> %s", idPublication, ex);
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }
}
