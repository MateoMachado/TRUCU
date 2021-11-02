package ucu.trucu.helper.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ucu.trucu.model.dao.PublicationDAO;
import ucu.trucu.model.dto.Publication;
import ucu.trucu.model.dto.Publication.PublicationStatus;

/**
 *
 * @author NicoPuig
 */
@Service
public class PublicationValidator {
    
    @Autowired
    private PublicationDAO publicationDAO;
    
    public void assertStatus(int idPublication, PublicationStatus expectedStatus) {
        Publication.PublicationStatus publicationStatus = publicationDAO.getStatus(idPublication);
        if (!expectedStatus.equals(publicationStatus)) {
            throw new IllegalStateException(String.format("Imposible ejecutar operacion en publicacion [idPublication=%s] con estado %s ",
                    idPublication, publicationStatus));
        }
    }
}
