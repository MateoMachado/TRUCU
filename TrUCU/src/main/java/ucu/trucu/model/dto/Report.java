package ucu.trucu.model.dto;

/**
 *
 * @author NicoPuig
 */
public class Report {

    private Integer idReport;
    private String status;
    private Integer idReason;

    public Integer getIdReport() {
        return idReport;
    }

    public void setIdReport(Integer idReport) {
        this.idReport = idReport;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdReason() {
        return idReason;
    }

    public void setIdReason(Integer idReason) {
        this.idReason = idReason;
    }
}