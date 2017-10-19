package application;

import javafx.application.Application;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainTest
{
    @Before
    public void setUp() throws Exception
    {
        System.out.printf("About to launch FX App\n");
        Thread t = new Thread("JavaFX Init Thread")
        {
            public void run()
            {
                Application.launch(application.Main.class);
            }
        };
        t.setDaemon(true);
        t.start();
        System.out.printf("FX App thread started\n");
        Thread.sleep(3000);
    }

    @Test
    public void testTitle() throws Exception
    {
        assertEquals("UniTerm", Main.getPrimaryStage().getTitle());
    }
}