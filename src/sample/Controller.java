package sample;

import com.fazecast.jSerialComm.*;
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
    private ComboBox<String> COMselector, baudRateComboBox;

    @FXML
    Button openButton, closeButton, sendButton;

    @FXML
    TextField sendTextField;

    @FXML
    TextArea receivedTextArea;

    @FXML
    RadioButton noParity, oddParity, evenParity;
    private final ToggleGroup parityGroup = new ToggleGroup();

    @FXML
    RadioButton eightBits, sevenBits, sixBits, fiveBits;
    private final ToggleGroup dataBitsGroup = new ToggleGroup();

    @FXML
    private RadioButton oneStopBit, twoStopBit;
    private final ToggleGroup stopBitsGroup = new ToggleGroup();

    @FXML
    Label statusBaudRateLabel;

    // ==========================================================================================
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        // ========================== Config radio buttons init =====================
        noParity.setToggleGroup(parityGroup);
        noParity.setSelected(true);
        noParity.setUserData(SerialPort.NO_PARITY);

        oddParity.setToggleGroup(parityGroup);
        oddParity.setUserData(SerialPort.ODD_PARITY);
        evenParity.setToggleGroup(parityGroup);
        evenParity.setUserData(SerialPort.EVEN_PARITY);

        eightBits.setToggleGroup(dataBitsGroup);
        eightBits.setSelected(true);
        eightBits.setUserData(8);

        sevenBits.setToggleGroup(dataBitsGroup);
        sevenBits.setUserData(7);
        sixBits.setToggleGroup(dataBitsGroup);
        sixBits.setUserData(6);
        fiveBits.setToggleGroup(dataBitsGroup);
        fiveBits.setUserData(5);

        oneStopBit.setToggleGroup(stopBitsGroup);
        oneStopBit.setSelected(true);
        oneStopBit.setUserData(SerialPort.ONE_STOP_BIT);
        twoStopBit.setToggleGroup(stopBitsGroup);
        twoStopBit.setUserData(SerialPort.TWO_STOP_BITS);

        // ======================= Config radio buttons events init ==================
        parityGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> Port.getInstance().setParity(newValue));
        dataBitsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> Port.getInstance().setNumDataBits(newValue));
        stopBitsGroup.selectedToggleProperty().addListener((observable, oldValue, newValue) -> Port.getInstance().setNumStopBits(newValue));
        // ===========================================================================

        // ==========================================================================
        for (SerialPort port : SerialPort.getCommPorts())
        {
            COMselector.getItems().add(port.getDescriptivePortName());
        }

        if(!COMselector.getItems().isEmpty())
            COMselector.getSelectionModel().select(0);
        // ==========================================================================

        // ============= Baud Rate ComboBox initial values ===========================
        baudRateComboBox.getItems().add("9600");
        baudRateComboBox.getItems().add("14400");
        baudRateComboBox.getItems().add("19200");
        baudRateComboBox.getItems().add("38400");
        baudRateComboBox.getItems().add("56000");
        baudRateComboBox.getItems().add("57600");
        baudRateComboBox.getItems().add("115200");
        baudRateComboBox.getItems().add("128000");
        baudRateComboBox.getItems().add("230400");
        baudRateComboBox.getItems().add("256000");
        baudRateComboBox.getItems().add("460800");
        baudRateComboBox.getItems().add("921600");
        baudRateComboBox.getItems().add("1000000");
        baudRateComboBox.getItems().add("2000000");
        baudRateComboBox.getItems().add("3000000");
        baudRateComboBox.getSelectionModel().select(String.valueOf(Port.getInstance().getBaudRate()));
        statusBaudRateLabel.setText(String.valueOf(Port.getInstance().getBaudRate()));
        // ===========================================================================

        baudRateComboBox.setOnAction(event -> baudRateComboBoxOnActionEvent());
        COMselector.setOnMouseClicked(event -> comSelectOnMouseClickedEvent());
        openButton.setOnMouseClicked(event -> openButtonEvent());
        closeButton.setOnMouseClicked(event -> closeButtonEvent());
        sendButton.setOnMouseClicked(event -> Port.getInstance().send(sendTextField.getText().getBytes()));

        sendTextField.setOnKeyPressed((KeyEvent key) ->
        {
            if (key.getCode().equals(KeyCode.ENTER))
            {
                Port.getInstance().send(sendTextField.getText().getBytes());
            }
        });
    }

    private void baudRateComboBoxOnActionEvent()
    {
        try
        {
            int baudRate = Integer.parseInt(baudRateComboBox.getSelectionModel().getSelectedItem());

            if (3000000 < baudRate)
            {
                baudRate = 3000000;
                baudRateComboBox.getSelectionModel().select(String.valueOf(baudRate));
            }

            System.out.println("Set baudRate to " + baudRate + ".");
            statusBaudRateLabel.setText(String.valueOf(baudRate));
            Port.getInstance().setBaudRate(baudRate);
        }
        catch (Exception ignored){}
    }

    // refresh available ports when combobox is expanded
    private void comSelectOnMouseClickedEvent()
    {
        String previous = COMselector.getSelectionModel().getSelectedItem();

        COMselector.getItems().clear();

        for (SerialPort port : SerialPort.getCommPorts())
        {
            COMselector.getItems().add(port.getDescriptivePortName());

            if ( port.getDescriptivePortName().equals(port.getDescriptivePortName()) )
            {
                COMselector.getSelectionModel().select(previous);
            }
        }
    }

    private void receivedData(byte[] newData, int numRead)
    {
        System.out.println("Read " + numRead + " bytes.");

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

    private void openButtonEvent()
    {
        if ( null != COMselector.getSelectionModel().getSelectedItem() )
        {
            String systemPortName = COMselector.getSelectionModel().getSelectedItem();
            systemPortName = systemPortName.substring(systemPortName.indexOf("(")+1, systemPortName.indexOf(")"));

            int dataBits = (int)dataBitsGroup.getSelectedToggle().getUserData();
            int stopBits = (int)stopBitsGroup.getSelectedToggle().getUserData();
            int parityBit = (int)parityGroup.getSelectedToggle().getUserData();

            Port.getInstance().addNewDataListener(this::receivedData);

            if (Port.getInstance().open(systemPortName, dataBits, stopBits, parityBit))
            {
                openButton.setDisable(true);
                closeButton.setDisable(false);
                System.out.println("Port is opened.");
            }
        }
    }

    private void closeButtonEvent()
    {
        Port.getInstance().close();

        openButton.setDisable(false);
        closeButton.setDisable(true);
        System.out.println("Port is closed.");
    }
}
