package ucu.trucu.model.dto;

import java.sql.Date;
import java.sql.Timestamp;

/**
 *
 * @author NicoPuig
 */
public class Publication {

    private Integer idPublication;
    private String title;
    private String description;
    private Integer ucuCoinValue;
    private Timestamp publicationDate;
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

    public Integer getUcuCoinValue() {
        return ucuCoinValue;
    }

    public void setUcuCoinValue(Integer ucuCoinValue) {
        this.ucuCoinValue = ucuCoinValue;
    }

    public Timestamp getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Timestamp publicationDate) {
        this.publicationDate = publicationDate;
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
}
