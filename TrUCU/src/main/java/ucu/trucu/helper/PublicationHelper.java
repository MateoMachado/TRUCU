package ucu.trucu.helper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.database.querybuilder.Filter;
import ucu.trucu.model.dao.PublicationDAO;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import ucu.trucu.model.dto.Publication;

/**
 *
 * @author NicoPuig
 */
@Service
public class PublicationHelper {

    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String UCUCOIN_VALUE = "ucuCoinValue";
    private static final String PUBLICATION_DATE = "publicationDate";
    private static final String STATUS = "status";
    private static final String ACCOUNT_CI = "accountCI";

    @Autowired
    private PublicationDAO publicationDAO;

    public Filter buildPublicationFilter(String title, String description, Integer maxUcuCoins,
            Integer minUcuCoins, Timestamp afterDate, Timestamp beforeDate, String status, String accountCI) {
        return Filter.build(where -> {
            List<String> conditions = new LinkedList<>();
            if (title != null) {
                conditions.add(where.eq(TITLE, title));
            }
            if (description != null) {
                conditions.add(where.eq(DESCRIPTION, description));
            }
            if (maxUcuCoins != null) {
                conditions.add(where.loet(UCUCOIN_VALUE, maxUcuCoins));
            }
            if (minUcuCoins != null) {
                conditions.add(where.goet(UCUCOIN_VALUE, minUcuCoins));
            }
            if (afterDate != null) {
                conditions.add(where.goet(PUBLICATION_DATE, afterDate));
            }
            if (beforeDate != null) {
                conditions.add(where.loet(PUBLICATION_DATE, beforeDate));
            }
            if (status != null) {
                conditions.add(where.eq(STATUS, status));
            }
            if (accountCI != null) {
                conditions.add(where.eq(ACCOUNT_CI, accountCI));
            }
            return conditions.isEmpty() ? null : where.and(conditions.toArray(new String[0]));
        });
    }

    public List<Publication> getPublications(int pageSize, int pageNumber, Filter filter) {
        return publicationDAO.filterPublications(pageSize, pageNumber, filter);
    }
}
