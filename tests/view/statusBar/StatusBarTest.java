package view.statusBar;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StatusBarTest
{
    @Test
    public void test_A_InitValues() throws Exception
    {
        assertEquals("Baud rate: 115200", StatusBar.getInstance().baudRateLabel.getText());
        assertEquals("Closed", StatusBar.getInstance().statePortLabel.getText());
        assertEquals("COM4", StatusBar.getInstance().chosenPortLabel.getText());
    }

    @Test
    public void test_B_OpenedStatus() throws Exception
    {
        StatusBar.getInstance().setOpenedPortStatus(false);

        assertEquals("Closed", StatusBar.getInstance().statePortLabel.getText());
    }

    @Test
    public void test_C_Baudrate() throws Exception
    {
        StatusBar.getInstance().setBaudrate("9600");

        assertEquals("Baud rate: 9600", StatusBar.getInstance().baudRateLabel.getText());
    }

    @Test
    public void test_D_ChosenPort() throws Exception
    {
        StatusBar.getInstance().setChosenPort("COM1");

        assertEquals("COM1", StatusBar.getInstance().chosenPortLabel.getText());
    }
}