/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ucu.trucu.helper;

import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.dao.ReportDAO;
import ucu.trucu.model.dto.Reason;
import ucu.trucu.model.dto.Report;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author Seba Mazzey
 */
@Service
public class ReportHelper {
    
    private static final Logger LOGGER = LoggerFactory.create(ReportHelper.class);
    
    @Autowired
    private ReportDAO reportDAO;
    
    public int createReport(Report newReport) throws SQLException {
        return reportDAO.insert(newReport);
    }
    
    public List<Report> getReports(int pageSize, int pageNumber, Filter filter) {
        return reportDAO.filterReports(pageSize, pageNumber, filter);
    }
    
    public List<Reason> getReportReasons(int idPublication) {
        return reportDAO.getReportReasons(idPublication);
    }
}
