package ucu.trucu.api;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ucu.trucu.database.DBController;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.helper.ReportHelper;
import ucu.trucu.model.api.PublicationWrapper;
import ucu.trucu.model.dto.Reason;
import ucu.trucu.model.dto.Report;
import ucu.trucu.util.api.Message;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author Seba Mazzey
 */
@RestController
@RequestMapping("trucu/report")
@CrossOrigin(origins = {"${trucu.front.url.local}", "${trucu.front.url.firebase}"})
public class ReportController {

    private static final Logger LOGGER = LoggerFactory.create(ReportController.class);

    @Autowired
    private ReportHelper reportHelper;

    @Autowired
    private DBController dbController;

    @PostMapping("/create")
    public ResponseEntity createReport(@RequestBody Report newReport) {
        try {
            int idReport = reportHelper.createReport(newReport);
            dbController.commit();
            LOGGER.info("Reporte [Report=%s] creado correctamente", idReport);
            return ResponseEntity.ok(new Message("Reporte creado correctamente"));
        } catch (SQLException ex) {
            LOGGER.error("Error al crear el reporte -> %s", ex.getMessage());
            dbController.rollback();
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @GetMapping("/getReportedPublications")
    public ResponseEntity<Page<PublicationWrapper>> getReportedPublications(
            @RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false) String status) {
        Filter filter = Filter.build(where -> where.eq("status", status));

        LOGGER.info("Obteniendo reportes filtradors por [%s]", filter);
        return ResponseEntity.ok(reportHelper.filterPublications(pageSize, pageNumber, filter));
    }

    @GetMapping("/reasons")
    public ResponseEntity<List<Reason>> getReasons() {
        return ResponseEntity.ok(reportHelper.getReasons());
    }
    
    @GetMapping("/reportReasons")
    public ResponseEntity<Map<Integer, Integer>> getReportReasons(@RequestParam int idPublication) {
        return ResponseEntity.ok(reportHelper.getReportReasons(idPublication));
    }

    @PostMapping("/acceptReport")
    public ResponseEntity acceptReport(@RequestParam int idPublication) {
        try {
            reportHelper.acceptReport(idPublication);
            dbController.commit();
            LOGGER.info("Reporte aceptado para la publicacion [idPublication=%s]", idPublication);
            return ResponseEntity.ok(new Message("Reporte aceptado correctamente"));
        } catch (SQLException ex) {
            dbController.rollback();
            LOGGER.error("Error al aceptar el reporte de la publicacion [idPublication=%s] -> %s", idPublication, ex);
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }

    @PostMapping("/cancelReport")
    public ResponseEntity cancelReport(@RequestParam int idPublication) {
        try {
            reportHelper.cancelReport(idPublication);
            dbController.commit();
            LOGGER.info("Reporte rechazado para la publicacion [idPublication=%s]", idPublication);
            return ResponseEntity.ok(new Message("Reporte rechazado correctamente"));
        } catch (SQLException ex) {
            dbController.rollback();
            LOGGER.error("Error al aceptar el reporte de la publicacion [idPublication=%s] -> %s", idPublication, ex);
            return ResponseEntity.badRequest().body(new Message(ex.getLocalizedMessage()));
        }
    }
}
