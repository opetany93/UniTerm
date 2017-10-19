package view.statusBar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import serialPort.Port;
import view.portTab.PortTabController;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusBar implements Initializable
{
    private static volatile StatusBar StatusBarINSTANCE;

    @FXML
    Label baudRateLabel, chosenPortLabel, statePortLabel;

    public void setChosenPort(String chosenPort)
    {
        if ( null != chosenPort )
        {
            chosenPortLabel.setText(chosenPort);
        }
    }

    public void setOpenedPortStatus(boolean state)
    {
        if (state)
        {
            statePortLabel.setText("Opened");
        }
        else
        {
            statePortLabel.setText("Closed");
        }
    }

    public void setBaudrate(String text)
    {
        baudRateLabel.setText("Baud rate: " + text);
    }

    public StatusBar()
    {
        StatusBarINSTANCE = this;
    }

    public static StatusBar getInstance()
    {
        return StatusBarINSTANCE;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        baudRateLabel.setText("Baud rate: " + String.valueOf(Port.getInstance().getBaudRate()));
        setOpenedPortStatus(false);
        setChosenPort(PortTabController.getInstance().getSelectedPort());
    }
}
