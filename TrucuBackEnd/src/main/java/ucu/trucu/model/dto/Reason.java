package ucu.trucu.model.dto;

/**
 *
 * @author NicoPuig
 */
public class Reason {

    private Integer idReason;
    private String description;

    public Integer getIdReason() {
        return idReason;
    }

    public void setIdReason(Integer idReason) {
        this.idReason = idReason;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return idReason.hashCode();
    }

    @Override
    public boolean equals(Object o) {

        // If the object is compared with itself then return true 
        if (o == this) {
            return true;
        }

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
        if (!(o instanceof Reason)) {
            return false;
        }

        // typecast o to Complex so that we can compare data members
        Reason c = (Reason) o;

        // Compare the data members and return accordingly
        return idReason == c.idReason;
    }
}
