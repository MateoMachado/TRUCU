package ucu.trucu.model.dto;

/**
 *
 * @author NicoPuig
 */
public class Image {

    private Integer idImage;
    private String path;
    private Integer idPublication;

    public Integer getIdImage() {
        return idImage;
    }

    public void setIdImage(Integer idImage) {
        this.idImage = idImage;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(Integer idPublication) {
        this.idPublication = idPublication;
    }
}
