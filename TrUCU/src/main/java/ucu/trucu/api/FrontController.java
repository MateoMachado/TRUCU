package ucu.trucu.api;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
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
    private URI frontURL;

    @Value("${trucu.front.open}")
    private boolean autoOpenFront;

    public void initFront() throws IOException {
        if (autoOpenFront) {
            try {
                Desktop.getDesktop().browse(frontURL);
            } catch (IOException ex) {
                LOGGER.error("Imposible ingresar a la URL %s -> %s", frontURL, ex);
                throw ex;
            }
        }
        LOGGER.info("############# Aplicacion iniciada correctamente -> Ingrese a %s #############", frontURL);
    }
}
