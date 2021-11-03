package ucu.trucu.model.dao;

import java.util.List;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.model.dto.Reason;
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
    public Report findByPK(Object... primaryKeys) {
        return findFirst(where -> where.eq("idReport", primaryKeys[0]));
    }

    public List<Report> filterReports(int pageSize, int pageNumber, Filter filter) {
        return dbController.executeQuery(
                QueryBuilder.selectFrom(getTable())
                        .where(filter)
                        .offset(pageSize * pageNumber)
                        .fetchNext(pageSize),
                getEntityClass()
        );
    }

    public List<Reason> getReportReasons(int idPublication) {
        return dbController.executeQuery(
                QueryBuilder.selectFrom("Reasons", true, "Reasons.*")
                        .joinOn("Report",
                                "Report.idReason = Reason.idReason")
                        .joinOn("Publication",
                                "Publication.idPublication = Report.idPublication")
                        .where(where -> where.eq("Publication.idPublication",
                                idPublication)),
                Reason.class
        );
    }
}
