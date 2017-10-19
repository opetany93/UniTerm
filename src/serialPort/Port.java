package serialPort;

import com.fazecast.jSerialComm.*;
import javafx.scene.control.Toggle;
import view.logTab.Logger;

import java.util.ArrayList;
import java.util.List;

public class Port
{
    private static volatile Port PortINSTANCE;
    private SerialPort serialPort;

    private int baudRate = 115200;

    private List<NewDataEvent> newDataEvents = new ArrayList<>();

    private Port()
    {

    }

    public static Port getInstance()
    {
        if (null == PortINSTANCE)
        {
            synchronized (Port.class)
            {
                if (null == PortINSTANCE)
                {
                    PortINSTANCE = new Port();
                }
            }
        }
        return PortINSTANCE;
    }

    public int getBaudRate()
    {
        return baudRate;
    }

    public void setBaudRate(int baudRate)
    {
        this.baudRate = baudRate;

        if( null != serialPort)
            serialPort.setBaudRate(baudRate);
    }


    public void addNewDataListener(NewDataEvent event)
    {
        newDataEvents.add(event);
    }

    // ==============================================================================
    public boolean open(String serialPortName, int dataBits, int stopBits, int parityBits)
    {
        if(null != serialPortName)
        {
            serialPort = SerialPort.getCommPort(serialPortName);
            serialPort.setComPortParameters(baudRate, dataBits, stopBits, parityBits);

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

                    for ( NewDataEvent evt : newDataEvents)
                        evt.dataReceivedEvent(newData, numRead);
                }
            });

            try
            {
                return (serialPort.openPort());
            }
            catch (Exception e){ return false; }
        }

        return false;
    }

    public void send(byte[] buffer)
    {
        if(null != serialPort)
        {
            if ( serialPort.isOpen() )
            {
                serialPort.writeBytes(buffer, buffer.length);
            }
        }
    }

    public boolean close()
    {
        boolean isOpened = false;

        if(null != serialPort)
        {
            isOpened = serialPort.closePort();

            if (!serialPort.isOpen())
            {
                serialPort = null;
            }
        }

        return isOpened;
    }

    public void setParity(Toggle newValue)
    {
        if( null != serialPort )
        {
            serialPort.setParity((int) newValue.getUserData());

            switch(serialPort.getParity())
            {
                case SerialPort.NO_PARITY:
                {
                    Logger.getInstance().log("No parity bit is set.");
                    break;
                }

                case SerialPort.ODD_PARITY:
                {
                    Logger.getInstance().log("Odd parity bit is set.");
                    break;
                }

                case SerialPort.EVEN_PARITY:
                {
                    Logger.getInstance().log("Even parity bit is set.");
                    break;
                }
            }
        }
    }

    public void setNumDataBits(Toggle newValue)
    {
        if( null != serialPort )
        {
            serialPort.setNumDataBits((int)newValue.getUserData());
            Logger.getInstance().log(serialPort.getNumDataBits() + " data bits is set.");
        }
    }

    public void setNumStopBits(Toggle newValue)
    {
        if( null != serialPort )
        {
            serialPort.setNumStopBits((int) newValue.getUserData());

            switch (serialPort.getNumStopBits())
            {
                case SerialPort.ONE_STOP_BIT:
                {
                    Logger.getInstance().log("1 stop bits is set.");

                    break;
                }
                case SerialPort.TWO_STOP_BITS:
                {
                    Logger.getInstance().log("2 stop bits is set.");

                    break;
                }
            }
        }
    }

    public String getSystemPortName()
    {
        if ( null != serialPort )
        {
            return serialPort.isOpen() ? serialPort.getSystemPortName() : "";
        }
        else
        {
            return "";
        }
    }
}
