package ucu.trucu.model.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import ucu.trucu.database.querybuilder.Filter;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author NicoPuig
 */
public class PublicationFilter implements DTOFilter {

    private static final String ID_PUBLICATION = "idPublication";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String UCUCOIN_VALUE = "ucuCoinValue";
    private static final String PUBLICATION_DATE = "publicationDate";
    private static final String STATUS = "status";
    private static final String ACCOUNT_CI = "accountCI";

    private Integer idPublication;
    private String title;
    private String description;
    private Integer maxUcuCoins;
    private Integer minUcuCoins;
    private Timestamp afterDate;
    private Timestamp beforeDate;
    private String status;
    private String accountCI;

    public Integer getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(Integer idPublication) {
        this.idPublication = idPublication;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxUcuCoins() {
        return maxUcuCoins;
    }

    public void setMaxUcuCoins(Integer maxUcuCoins) {
        this.maxUcuCoins = maxUcuCoins;
    }

    public Integer getMinUcuCoins() {
        return minUcuCoins;
    }

    public void setMinUcuCoins(Integer minUcuCoins) {
        this.minUcuCoins = minUcuCoins;
    }

    public Timestamp getAfterDate() {
        return afterDate;
    }

    public void setAfterDate(Timestamp afterDate) {
        this.afterDate = afterDate;
    }

    public Timestamp getBeforeDate() {
        return beforeDate;
    }

    public void setBeforeDate(Timestamp beforeDate) {
        this.beforeDate = beforeDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAccountCI() {
        return accountCI;
    }

    public void setAccountCI(String accountCI) {
        this.accountCI = accountCI;
    }

    @Override
    public Filter toFilter() {
        return Filter.build(where -> {
            List<String> conditions = new LinkedList<>();
            if (idPublication != null) {
                conditions.add(where.eq(ID_PUBLICATION, idPublication));
            }
            if (title != null) {
                conditions.add(where.like(TITLE, String.format("%%%s%%", title)));
            }
            if (description != null) {
                conditions.add(where.like(DESCRIPTION, String.format("%%%s%%", description)));
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
}
