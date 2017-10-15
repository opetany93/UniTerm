package view;

import serialPort.Port;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.*;


public class Controller implements Initializable
{
    // ================== FXML =================================================================
    @FXML
    Button sendButton;

    @FXML
    TextField sendTextField;

    @FXML
    Tab plotterTab;

    @FXML
    Button startButton, stopButton;

    // ==========================================================================================
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        sendButton.setOnMouseClicked(event -> Port.getInstance().send(sendTextField.getText().getBytes()));

        sendTextField.setOnKeyPressed((KeyEvent key) ->
        {
            if (key.getCode().equals(KeyCode.ENTER))
            {
                Port.getInstance().send(sendTextField.getText().getBytes());
            }
        });

        Plot plot = new Plot();
        plotterTab.setContent(plot.createContent());

        startButton.setOnMouseClicked(event -> plot.play());

        stopButton.setOnMouseClicked(event -> plot.stop());
    }
}
