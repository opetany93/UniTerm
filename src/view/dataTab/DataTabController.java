package view.dataTab;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import serialPort.Port;
import view.logTab.Logger;

import java.net.URL;
import java.util.ResourceBundle;

public class DataTabController implements Initializable
{
    private static volatile DataTabController DataTabControllerINSTANCE;

    @FXML
    TextArea receivedTextArea;

    public DataTabController()
    {
        DataTabControllerINSTANCE = this;
    }

    public static DataTabController getInstance()
    {
        return DataTabControllerINSTANCE;
    }

    private void receivedData(byte[] newData, int numRead)
    {
        Logger.getInstance().log("Read " + numRead + " bytes.");

        for( int i = 0; i < newData.length; i++ )
        {
            if ( (newData[i] == '\\')  && (newData[i+1] == 'n') )
            {
                receivedTextArea.appendText("\n");
                i++;
            }
            else
            {
                receivedTextArea.appendText((char)newData[i] + "");
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        Port.getInstance().addNewDataListener(this::receivedData);
    }
}
