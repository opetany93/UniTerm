package serialPort;

public interface NewDataEvent
{
    void dataReceivedEvent(byte[] newData, int numRead);
}
