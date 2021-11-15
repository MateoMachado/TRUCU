package ucu.trucu.api;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import ucu.trucu.util.log.Logger;
import ucu.trucu.util.log.LoggerFactory;

/**
 *
 * @author NicoPuig
 */
@Controller
public class FrontController {

    private static final Logger LOGGER = LoggerFactory.create(FrontController.class);

    @Value("${trucu.front.url.firebase}")
    private String frontURL;

    @Value("${trucu.front.open}")
    private boolean autoOpenFront;

    @Value("${trucu.front.initialPage}")
    private String initialFrontPage;

    public void initFront() throws IOException, URISyntaxException {
        String completeURI = frontURL + initialFrontPage;
        if (autoOpenFront) {
            try {
                Desktop.getDesktop().browse(new URI(completeURI));
            } catch (IOException | URISyntaxException ex) {
                LOGGER.error("Imposible ingresar a la URL %s -> %s", completeURI, ex);
                throw ex;
            }
        }
        LOGGER.info("############# Aplicacion iniciada correctamente -> Ingrese a %s #############", completeURI);
    }
}
