package ucu.trucu.model.dao;

import org.springframework.stereotype.Component;
import ucu.trucu.model.dto.Image;

/**
 *
 * @author NicoPuig
 */
@Component
public class ImageDAO extends AbstractDAO<Image> {

    @Override
    public String getTable() {
        return "Image";
    }

    @Override
    public Class<Image> getEntityClass() {
        return Image.class;
    }
}
