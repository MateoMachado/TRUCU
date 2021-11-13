package ucu.trucu.model.dao;

import java.sql.SQLException;
import java.util.List;
import org.springframework.stereotype.Component;
import ucu.trucu.database.querybuilder.QueryBuilder;
import ucu.trucu.database.querybuilder.statement.InsertStatement;
import static ucu.trucu.model.dao.PublicationDAO.ID_PUBLICATION;
import ucu.trucu.model.dto.Image;

/**
 *
 * @author NicoPuig
 */
@Component
public class ImageDAO extends AbstractDAO<Image> {

    public static final String ID_IMAGE = "idImage";
    public static final String IMAGE_BYTES = "imageBytes";
    public static final String IMAGE = "Image";

    @Override
    public String getTable() {
        return IMAGE;
    }

    @Override
    public Class<Image> getEntityClass() {
        return Image.class;
    }

    @Override
    public Image findByPK(Object... primaryKeys) {
        return findFirst(where -> where.eq(ID_IMAGE, primaryKeys[0]));
    }

    public List<Integer> addPublicationImages(int idPublication, List<Image> images) throws SQLException {
        InsertStatement insert = QueryBuilder
                .insertInto(getTable())
                .keys(IMAGE_BYTES, ID_PUBLICATION);

        images.forEach(image -> insert
                .values(image.getImageBytes(), idPublication));

        return dbController.executeInsert(insert);
    }
}
