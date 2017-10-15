package view.statusBar;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import serialPort.Port;

import java.net.URL;
import java.util.ResourceBundle;

public class StatusBar implements Initializable
{
    private static volatile StatusBar StatusBarINSTANCE;

    @FXML
    Label statusBaudRateLabel;

    public void setBaudrate(String text)
    {
        statusBaudRateLabel.setText("Baud rate: " + text);
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
        statusBaudRateLabel.setText("Baud rate: " + String.valueOf(Port.getInstance().getBaudRate()));
    }
}
