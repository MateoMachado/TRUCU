/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ucu.trucu.helper;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.api.PublicationWrapper;
import ucu.trucu.model.dao.ImageDAO;
import ucu.trucu.model.dao.PublicationDAO;
import ucu.trucu.model.dao.ReportDAO;
import ucu.trucu.model.dto.Image;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Reason;
import ucu.trucu.model.dto.Report;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;
import ucu.trucu.util.pagination.Page;

/**
 *
 * @author Seba Mazzey
 */
@Service
public class ReportHelper {
    
    private static final Logger LOGGER = LoggerFactory.create(ReportHelper.class);
    
    @Autowired
    private ReportDAO reportDAO;
    
    @Autowired
    private ImageDAO imageDAO;
    
    public int createReport(Report newReport) throws SQLException {
        return reportDAO.insert(newReport);
    }
    
    public Page<PublicationWrapper> getPublications(int pageSize, int pageNumber, Filter filter) {
        List<Report> output = reportDAO.filterReports(pageSize, pageNumber, filter);
        return null ;
    }
    
    public Page<PublicationWrapper> filterPublications(int pageSize, int pageNumber, Filter filter) {
        int totalPublications = reportDAO.count(filter);
        List<Publication> publications = reportDAO.filterPublications(pageSize, pageNumber, filter);
        List<Integer> publicationIds = new LinkedList<>();
        publications.forEach(publication -> publicationIds.add(publication.getIdPublication()));
        List<Image> images = imageDAO.findBy(where -> where.in(PublicationDAO.ID_PUBLICATION, publicationIds));
        List<PublicationWrapper> wrappers = new LinkedList<>();
        publications.forEach(publication -> {
            wrappers.add(new PublicationWrapper(
                    publication,
                    images.stream()
                            .filter(image -> image.getIdPublication().equals(publication.getIdPublication()))
                            .collect(Collectors.toList()))
            );
        });

        return new Page<>(totalPublications, pageNumber, pageSize, wrappers);
    }
    
    public Map<Reason, Integer> getReportReasons(int idPublication) {
        Map<Reason, Integer> reasonCount = new HashMap<>();
        List<Reason> reasons = reportDAO.getReportReasons(idPublication);
        // Cuento las ocurrencias de cada motivo
        reasons.forEach(t -> {
            reasonCount.put(t, reasonCount.getOrDefault(t, 0) + 1);
        });
        
        return reasonCount;
    }
}
