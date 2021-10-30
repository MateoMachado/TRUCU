package ucu.trucu.api;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.dao.PublicationDAO;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@RestController
@RequestMapping("trucu/publication")
public class PublicationController {

    private static final Logger LOGGER = LoggerFactory.create(PublicationController.class);

    @Autowired
    private PublicationDAO publicationDAO;

    @GetMapping("/filter")
    public ResponseEntity<List<Publication>> getUserPublications(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer maxUcuCoins,
            @RequestParam(required = false) Integer minUcuCoins,
            @RequestParam(required = false) Date afterDate,
            @RequestParam(required = false) Date beforeDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String accountCI
    ) {
        
        LOGGER.info("Solicitud de Publicaciones");
        Filter filter = Filter.build(where -> {
            List<String> conditions = new LinkedList<>();
            if (title != null) {
                conditions.add(where.eq("title", title));
            }
            if (description != null) {
                conditions.add(where.eq("description", description));
            }
            if (maxUcuCoins != null) {
                conditions.add(where.loet("ucuCoinValue", maxUcuCoins));
            }
            if (minUcuCoins != null) {
                conditions.add(where.goet("ucuCoinValue", minUcuCoins));
            }
            if (afterDate != null) {
                conditions.add(where.goet("publicationDate", afterDate));
            }
            if (beforeDate != null) {
                conditions.add(where.loet("publicationDate", beforeDate));
            }
            if (status != null) {
                conditions.add(where.eq("status", status));
            }
            if (accountCI != null) {
                conditions.add(where.eq("accountCI", accountCI));
            }
            return where.and(conditions.toArray(new String[0]));
        });
        return ResponseEntity.ok(publicationDAO.filterPublications(pageSize, pageNumber, filter));
    }
}
