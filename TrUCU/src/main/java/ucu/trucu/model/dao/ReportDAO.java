package ucu.trucu.model.dao;

import org.springframework.stereotype.Component;
import ucu.trucu.model.dto.Report;

/**
 *
 * @author NicoPuig
 */
@Component
public class ReportDAO extends AbstractDAO<Report> {

    @Override
    public String getTable() {
        return "Report";
    }

    @Override
    public Class<Report> getEntityClass() {
        return Report.class;
    }

    @Override
    public Report findByPK(String... primaryKeys) {
        return findFirst(where -> where.eq("idReport", primaryKeys[0]));
    }
}