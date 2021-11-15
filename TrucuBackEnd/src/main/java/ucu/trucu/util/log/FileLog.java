package ucu.trucu.util.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author NicoPuig
 */
public class FileLog implements Log {

    private static final String DEFAULT_FILE_PATH = "log.txt";
    private File file;
    private boolean working;

    public FileLog() {
        deleteLastLog();
    }

    private void deleteLastLog() {
        try {
            File lastLog = new File(System.getProperty("user.dir"), DEFAULT_FILE_PATH);
            lastLog.delete();
            file = new File(System.getProperty("user.dir"), DEFAULT_FILE_PATH);
            file.createNewFile();
            working = true;
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void log(String message) {
        if (working) {
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, true))) {
                bw.write(message + "\n");
            } catch (IOException ex) {
                System.out.println(ex);
            }
        }
    }
}
