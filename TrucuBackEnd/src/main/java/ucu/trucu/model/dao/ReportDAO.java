package ucu.trucu.model.dao;

import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.Count;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.database.querybuilder.QueryBuilder;
import static ucu.trucu.model.dao.PublicationDAO.ID_PUBLICATION;
import static ucu.trucu.model.dao.PublicationDAO.PUBLICATION;
import static ucu.trucu.model.dao.PublicationDAO.PUBLICATION_DATE;
import static ucu.trucu.model.dao.ReasonDAO.ID_REASON;
import static ucu.trucu.model.dao.ReasonDAO.REASON;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Reason;
import ucu.trucu.model.dto.Report;

/**
 *
 * @author NicoPuig
 */
@Component
public class ReportDAO extends AbstractDAO<Report> {

    public static final String REPORT = "Report";
    public static final String ID_REPORT = "idReport";
    public static final String STATUS = "status";

    @Override
    public String getTable() {
        return REPORT;
    }

    @Override
    public Class<Report> getEntityClass() {
        return Report.class;
    }

    @Override
    public Report findByPK(Object... primaryKeys) {
        return findFirst(where -> where.eq(ID_REPORT, primaryKeys[0]));
    }

    public List<Report> filterReports(int pageSize, int pageNumber, Filter filter) {
        return dbController.executeQuery(
                QueryBuilder.selectFrom(getTable())
                        .where(filter)
                        .orderDesc(ID_REPORT)
                        .offset(pageSize * pageNumber)
                        .fetchNext(pageSize),
                getEntityClass()
        );
    }

    public List<Reason> getReportReasons(int idPublication) {
        return dbController.executeQuery(
                QueryBuilder.selectFrom(REASON, REASON + ".*")
                        .joinOnId(REPORT, ID_REASON)
                        .joinOn(PUBLICATION, String.format("%s.%s = %s.%s", REPORT, ID_PUBLICATION, PUBLICATION, ID_PUBLICATION))
                        .where(where -> where.eq(PUBLICATION + "." + ID_PUBLICATION, idPublication)),
                Reason.class
        );
    }

    @Override
    public int count(Filter filter) {
        return dbController.executeQuery(QueryBuilder
                .selectFrom(REPORT, "COUNT(DISTINCT " + ID_PUBLICATION + ") AS 'count'")
                .where(filter),
                Count.class).get(0).getCount();
    }

    public List<Publication> filterPublications(int pageSize, int pageNumber, Filter filter) {
        return dbController.executeQuery(
                QueryBuilder.selectFrom(PUBLICATION)
                        .where(filter.in(ID_PUBLICATION,
                                QueryBuilder.selectDistinctFrom(REPORT, ID_PUBLICATION)
                                .where(filter2 -> filter2.eq(STATUS, Report.ReportStatus.OPEN))
                        ))
                        .orderDesc(PUBLICATION_DATE)
                        .offset(pageSize * pageNumber)
                        .fetchNext(pageSize),
                Publication.class
        );
    }

    public void acceptReport(int idPublication) throws SQLException {
        dbController.executeUpdate(
                QueryBuilder.update(REPORT)
                        .set(STATUS, Report.ReportStatus.ACCEPTED)
                        .where(where -> where.eq(ID_PUBLICATION, idPublication))
        );
    }

    public void cancelReport(int idPublication) throws SQLException {
        dbController.executeUpdate(
                QueryBuilder.update(REPORT)
                        .set(STATUS, Report.ReportStatus.REJECTED)
                        .where(filter -> filter.eq(ID_PUBLICATION, idPublication))
        );
    }

    public void cancelPublicationReports(int idPublication) throws SQLException {
        dbController.executeUpdate(
                QueryBuilder.deleteFrom(REPORT)
                        .where(filter -> filter.eq(ID_PUBLICATION, idPublication))
        );
    }
}
