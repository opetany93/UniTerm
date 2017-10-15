package view.logTab;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger
{
    private static volatile Logger LoggerINSTANCE;

    @FXML
    TextArea logTextArea;

    public Logger()
    {
        LoggerINSTANCE = this;
    }

    public static Logger getInstance()
    {
        return LoggerINSTANCE;
    }

    public void log(String text)
    {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();

        if(null != LoggerINSTANCE)
        {
            logTextArea.appendText("[" + dateFormat.format(date) +"] " +text);
            logTextArea.appendText("\n");
        }
    }
}
