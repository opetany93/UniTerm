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
    private SerialPort serialPort;

    private SerialPort[] comPorts;
    private int baudRate;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        baudRate = 115200;

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
        // ==========================================================================

        comPorts = SerialPort.getCommPorts();

        for (SerialPort port : comPorts)
        {
            COMselector.getItems().add(port.getDescriptivePortName());
        }

        if(!COMselector.getItems().isEmpty())
            COMselector.getSelectionModel().select(0);

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
        baudRateComboBox.getSelectionModel().select(String.valueOf(baudRate));
        statusBaudRateLabel.setText(String.valueOf(baudRate));
        // ===========================================================================

        noParity.setOnAction(event ->
        {
            System.out.println("No parity bit chosen");

            if( null != serialPort )
                serialPort.setParity(SerialPort.NO_PARITY);
        });
        oddParity.setOnAction(event ->
        {
            System.out.println("Odd parity bit chosen");

            if( null != serialPort )
                serialPort.setParity(SerialPort.ODD_PARITY);
        });
        evenParity.setOnAction(event ->
        {
            System.out.println("Even parity bit chosen");

            if( null != serialPort )
                serialPort.setParity(SerialPort.EVEN_PARITY);
        });

        eightBits.setOnAction(event ->
        {
            System.out.println("8 data bits chosen.");

            if( null != serialPort )
                serialPort.setNumDataBits(8);
        });
        sevenBits.setOnAction(event ->
        {
            System.out.println("7 data bits chosen.");

            if( null != serialPort )
                serialPort.setNumDataBits(7);
        });
        sixBits.setOnAction(event ->
        {
            System.out.println("6 data bits chosen.");

            if( null != serialPort )
                serialPort.setNumDataBits(6);
        });
        fiveBits.setOnAction(event ->
        {
            System.out.println("5 data bits chosen.");

            if( null != serialPort )
                serialPort.setNumDataBits(5);
        });

        oneStopBit.setOnAction(event ->
        {
            System.out.println("1 stop bit chosen.");

            if( null != serialPort )
                serialPort.setNumStopBits(SerialPort.ONE_STOP_BIT);
        });
        twoStopBit.setOnAction(event ->
        {
            System.out.println("2 stop bit chosen.");

            if( null != serialPort )
                serialPort.setNumStopBits(SerialPort.TWO_STOP_BITS);
        });

        baudRateComboBox.setOnAction(event ->
        {
            try
            {
                baudRate = Integer.parseInt(baudRateComboBox.getSelectionModel().getSelectedItem());
                if (3000000 < baudRate)
                    baudRate = 3000000;
                System.out.println("Set baudRate to " + baudRate + ".");
                baudRateComboBox.getSelectionModel().select(String.valueOf(baudRate));
                statusBaudRateLabel.setText(String.valueOf(baudRate));

                if( null != serialPort)
                    serialPort.setBaudRate(baudRate);
            }
            catch (Exception ignored){}
        });

        COMselector.setOnMouseClicked(event ->
        {
            String temp = COMselector.getSelectionModel().getSelectedItem();

            COMselector.getItems().clear();
            comPorts = SerialPort.getCommPorts();

            for (SerialPort port : comPorts)
            {
                COMselector.getItems().add(port.getDescriptivePortName());

                if ( port.getDescriptivePortName().equals(port.getDescriptivePortName()) )
                {
                    COMselector.getSelectionModel().select(temp);
                }
            }
        });

        openButton.setOnMouseClicked(event -> openButtonEvent());
        closeButton.setOnMouseClicked(event -> closeButtonEvent());
        sendButton.setOnMouseClicked(event -> sendButtonEvent());

        sendTextField.setOnKeyPressed((KeyEvent key) ->
        {
            if (key.getCode().equals(KeyCode.ENTER))
            {
                sendButtonEvent();
            }
        });
    }

    private void sendButtonEvent()
    {
        byte[] buffer;

        buffer = sendTextField.getText().getBytes();

        if(null != serialPort)
        {
            if ( serialPort.isOpen() )
            {
                serialPort.writeBytes(buffer, buffer.length);
            }
        }
    }

    private void openButtonEvent()
    {
        if ( null != COMselector.getSelectionModel().getSelectedItem() )
        {
            String systemPortName = COMselector.getSelectionModel().getSelectedItem();

            serialPort = SerialPort.getCommPort(systemPortName.substring(systemPortName.indexOf("(")+1, systemPortName.indexOf(")")));

            serialPort.setComPortParameters(baudRate, (int)dataBitsGroup.getSelectedToggle().getUserData(), (int)stopBitsGroup.getSelectedToggle().getUserData(), (int)parityGroup.getSelectedToggle().getUserData());

            try
            {
                serialPort.openPort();
            }
            catch (Exception ignored){}

            if (serialPort.isOpen())
            {
                openButton.setDisable(true);
                closeButton.setDisable(false);
                System.out.println("Port is opened.");
            }
        }

        serialPort.addDataListener(new SerialPortDataListener()
        {
            @Override
            public int getListeningEvents() { return SerialPort.LISTENING_EVENT_DATA_AVAILABLE; }
            @Override
            public void serialEvent(SerialPortEvent event)
            {
                if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
                    return;
                byte[] newData = new byte[serialPort.bytesAvailable()];
                int numRead = serialPort.readBytes(newData, newData.length);
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
        });
    }

    private void closeButtonEvent()
    {
        serialPort.closePort();

        if (!serialPort.isOpen())
        {
            openButton.setDisable(false);
            closeButton.setDisable(true);
            System.out.println("Port is closed.");
            serialPort = null;
        }
    }
}
