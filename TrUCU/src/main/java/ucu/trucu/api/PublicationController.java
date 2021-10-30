package ucu.trucu.api;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.helper.PublicationHelper;
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
    private PublicationHelper publicationHelper;

    @GetMapping("/filter")
    public ResponseEntity<List<Publication>> getUserPublications(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) Integer maxUcuCoins,
            @RequestParam(required = false) Integer minUcuCoins,
            @RequestParam(required = false) Timestamp afterDate,
            @RequestParam(required = false) Timestamp beforeDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String accountCI
    ) {

        Filter filter = publicationHelper.buildPublicationFilter(title, description, maxUcuCoins,
                minUcuCoins, afterDate, beforeDate, status, accountCI);

        LOGGER.info("Obteniendo publicaciones filtradas por [%s]", filter);
        return ResponseEntity.ok(publicationHelper.getPublications(pageSize, pageNumber, filter));
    }
}
