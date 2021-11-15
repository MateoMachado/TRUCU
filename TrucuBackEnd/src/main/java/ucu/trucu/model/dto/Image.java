package ucu.trucu.model.dto;

/**
 *
 * @author NicoPuig
 */
public class Image {

    private Integer idImage;
    private String imageBytes;
    private Integer idPublication;

    public Integer getIdImage() {
        return idImage;
    }

    public void setIdImage(Integer idImage) {
        this.idImage = idImage;
    }

    public String getImageBytes() {
        return imageBytes;
    }

    public void setImageBytes(String imageBytes) {
        this.imageBytes = imageBytes;
    }

    public Integer getIdPublication() {
        return idPublication;
    }

    public void setIdPublication(Integer idPublication) {
        this.idPublication = idPublication;
    }
}
