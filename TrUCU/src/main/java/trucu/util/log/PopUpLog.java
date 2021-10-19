package trucu.util.log;

import javax.swing.JOptionPane;

/**
 *
 * @author NicoPuig
 */
public class PopUpLog implements Log {

    public static void createPopUp(String title, String message) {        
        JOptionPane.showMessageDialog(null, message, "LOG", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void log(String message) {
        createPopUp("LOG", message);
    }
}
