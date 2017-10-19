package view.dataTab;

import org.junit.Test;

import static org.junit.Assert.*;

public class DataTabControllerTest
{
    @Test
    public void testTextArea() throws Exception
    {
        DataTabController.getInstance().receivedTextArea.appendText("TEST");

        assertEquals("TEST", DataTabController.getInstance().receivedTextArea.getText());
    }
}