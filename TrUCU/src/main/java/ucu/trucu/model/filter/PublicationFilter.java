package ucu.trucu.model.filter;

import ucu.trucu.database.querybuilder.Filter;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;
import ucu.trucu.model.dao.PublicationDAO;

/**
 *
 * @author NicoPuig
 */
public class PublicationFilter implements DTOFilter {

    private Integer idPublication;
    private String title;
    private String description;
    private Integer maxUcuCoins;
    private Integer minUcuCoins;
    private Timestamp afterDate;
    private Timestamp beforeDate;
    private String[] status;
    private String accountEmail;

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

    public String[] getStatus() {
        return status;
    }

    public void setStatus(String[] status) {
        this.status = status;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    @Override
    public Filter toFilter() {
        return Filter.build(where -> {
            List<String> conditions = new LinkedList<>();
            if (idPublication != null) {
                conditions.add(where.eq(PublicationDAO.ID_PUBLICATION, idPublication));
            }
            if (title != null) {
                conditions.add(where.like(PublicationDAO.TITLE, String.format("%%%s%%", title)));
            }
            if (description != null) {
                conditions.add(where.like(PublicationDAO.DESCRIPTION, String.format("%%%s%%", description)));
            }
            if (maxUcuCoins != null) {
                conditions.add(where.loet(PublicationDAO.UCUCOIN_VALUE, maxUcuCoins));
            }
            if (minUcuCoins != null) {
                conditions.add(where.goet(PublicationDAO.UCUCOIN_VALUE, minUcuCoins));
            }
            if (afterDate != null) {
                conditions.add(where.goet(PublicationDAO.PUBLICATION_DATE, afterDate));
            }
            if (beforeDate != null) {
                conditions.add(where.loet(PublicationDAO.PUBLICATION_DATE, beforeDate));
            }
            if (status != null) {
                conditions.add(where.in(PublicationDAO.STATUS, status));
            }
            if (accountEmail != null) {
                conditions.add(where.eq(PublicationDAO.ACCOUNT_EMAIL, accountEmail));
            }
            return conditions.isEmpty() ? null : where.and(conditions.toArray(new String[0]));
        });
    }
}
