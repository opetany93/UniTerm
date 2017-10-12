package sample;

import com.fazecast.jSerialComm.*;
import javafx.scene.control.Toggle;

import java.util.ArrayList;
import java.util.List;

interface NewDataEvent
{
    void dataReceivedEvent(byte[] newData, int numRead);
}

class Port
{
    private static volatile Port PortINSTANCE;
    private SerialPort serialPort;

    private int baudRate = 115200;

    private List<NewDataEvent> newDataEvents = new ArrayList<>();

    private Port()
    {

    }

    static Port getInstance()
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

    int getBaudRate()
    {
        return baudRate;
    }

    void setBaudRate(int baudRate)
    {
        this.baudRate = baudRate;

        if( null != serialPort)
            serialPort.setBaudRate(baudRate);
    }


    void addNewDataListener(NewDataEvent event)
    {
        newDataEvents.add(event);
    }

    // ==============================================================================
    boolean open(String serialPortName, int dataBits, int stopBits, int parityBits)
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

    void send(byte[] buffer)
    {
        if(null != serialPort)
        {
            if ( serialPort.isOpen() )
            {
                serialPort.writeBytes(buffer, buffer.length);
            }
        }
    }

    void close()
    {
        if(null != serialPort)
        {
            serialPort.closePort();

            if (!serialPort.isOpen())
            {
                serialPort = null;
            }
        }
    }

    void setParity(Toggle newValue)
    {
        if( null != serialPort )
        {
            serialPort.setParity((int) newValue.getUserData());

            switch(serialPort.getParity())
            {
                case SerialPort.NO_PARITY:
                {
                    System.out.println("No parity bit is set.");
                    break;
                }

                case SerialPort.ODD_PARITY:
                {
                    System.out.println("Odd parity bit is set.");
                    break;
                }

                case SerialPort.EVEN_PARITY:
                {
                    System.out.println("Even parity bit is set.");
                    break;
                }
            }
        }
    }

    void setNumDataBits(Toggle newValue)
    {
        if( null != serialPort )
        {
            serialPort.setNumDataBits((int)newValue.getUserData());
            System.out.println(serialPort.getNumDataBits() + " data bits is set.");
        }
    }

    void setNumStopBits(Toggle newValue)
    {
        if( null != serialPort )
        {
            serialPort.setNumStopBits((int) newValue.getUserData());

            switch (serialPort.getNumStopBits())
            {
                case SerialPort.ONE_STOP_BIT:
                {
                    System.out.println("1 stop bits is set.");

                    break;
                }
                case SerialPort.TWO_STOP_BITS:
                {
                    System.out.println("2 stop bits is set.");

                    break;
                }
            }
        }
    }
}
