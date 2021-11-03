/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ucu.trucu.api;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.helper.ReportHelper;
import ucu.trucu.model.dto.Reason;
import ucu.trucu.model.dto.Report;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author Seba Mazzey
 */
@RestController
@RequestMapping("trucu/report")
public class ReportController {
    
    private static final Logger LOGGER = LoggerFactory.create(ReportController.class);
    
    @Autowired
    private ReportHelper reportHelper;
    
    @PostMapping("/create")
    public ResponseEntity createReport(@RequestBody Report newReport) {
        try {
            int idReport = reportHelper.createReport(newReport);
            LOGGER.info("Reporte [Report=%s] creado correctamente", idReport);
            return ResponseEntity.ok("Reporte creado correctamente");
        } catch (SQLException ex) {
            LOGGER.error("Error al crear el reporte -> %s", ex.getMessage());
            return ResponseEntity.badRequest().body(ex.getLocalizedMessage());
        }
    }
    
    @PostMapping("/getReports")
    public ResponseEntity<List<Report>> getReportedPublications(
            @RequestParam(defaultValue = "0") int pageSize,
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(required = false) String status) {
        Filter filter = Filter.build(where -> where.eq("status", status));
        
        LOGGER.info("Obteniendo reportes filtradors por [%s]", filter);
        return ResponseEntity.ok(reportHelper.getReports(pageSize, pageNumber, filter));
    }
    
    @GetMapping("/reasons")
    public ResponseEntity<List<Reason>> getReportReasons(@RequestParam int idPublication) {
        return ResponseEntity.ok(reportHelper.getReportReasons(idPublication));
    }
}
