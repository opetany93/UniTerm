package view;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.scene.Parent;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.util.Duration;

public class Plot
{
    private XYChart.Series<Number, Number> hourDataSeries;
    private XYChart.Series<Number, Number> minuteDataSeries;
    private NumberAxis xAxis;
    private Timeline animation;
    private double hours = 0;
    private double minutes = 0;
    private double timeInHours = 0;
    private double prevY = 10;
    private double y = 10;

    public Plot()
    {
        // create timeline to add new data every 60th of second
        animation = new Timeline();

        animation.getKeyFrames().add(new KeyFrame(Duration.millis(1000 / 100), (ActionEvent actionEvent) ->
        {
            // 6 minutes data per frame
            for (int count = 0; count < 6; count++) {
                nextTime();
                plotTime();
            }
        }));

        animation.setCycleCount(Animation.INDEFINITE);
    }

    Parent createContent()
    {
        xAxis = new NumberAxis(0, 24, 3);
        final NumberAxis yAxis = new NumberAxis(0, 100, 10);
        LineChart<Number, Number> chart = new LineChart<>(xAxis, yAxis);
        // setup chart
        chart.getStylesheets().add(getClass().getResource("/application/styles.css").toExternalForm());
        chart.setCreateSymbols(false);
        chart.setAnimated(false);
        chart.setLegendVisible(false);
        //chart.setTitle("RealTimeData");
        xAxis.setLabel("Time");
        xAxis.setForceZeroInRange(false);
        yAxis.setLabel("Data");
        yAxis
                .setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis, "$", null));
        // add starting data
        hourDataSeries = new XYChart.Series<>();
        hourDataSeries.setName("Hourly Data");
        minuteDataSeries = new XYChart.Series<>();
        minuteDataSeries.setName("Minute Data");
        // create some starting data
        hourDataSeries.getData()
                .add(new XYChart.Data<>(timeInHours, prevY));
        minuteDataSeries.getData()
                .add(new XYChart.Data<>(timeInHours, prevY));
        for (double m = 0; m < (60); m++)
        {
            nextTime();
            plotTime();
        }
        chart.getData().add(minuteDataSeries);
        chart.getData().add(hourDataSeries);
        return chart;
    }

    private void nextTime()
    {
        if (minutes == 59)
        {
            hours++;
            minutes = 0;
        } else
        {
            minutes++;
        }
        timeInHours = hours + ((1d / 60d) * minutes);
    }

    private void plotTime()
    {
        if ((timeInHours % 1) == 0)
        {
            // change of hour
            double oldY = y;
            y = prevY - 10 + (Math.random() * 20);
            prevY = oldY;
            while (y < 10 || y > 90) {
                y = y - 10 + (Math.random() * 20);
            }
            hourDataSeries.getData()
                    .add(new XYChart.Data<>(timeInHours, prevY));
            // after 25hours delete old data
            if (timeInHours > 25)
            {
                hourDataSeries.getData().remove(0);
            }
            // every hour after 24 move range 1 hour
            if (timeInHours > 24)
            {
                xAxis.setLowerBound(xAxis.getLowerBound() + 1);
                xAxis.setUpperBound(xAxis.getUpperBound() + 1);
            }
        }
        double min = (timeInHours % 1);
        double randomPickVariance = Math.random();
        if (randomPickVariance < 0.3)
        {
            double minY = prevY + ((y - prevY) * min) - 4 + (Math.random() * 8);
            minuteDataSeries.getData().add(new XYChart.Data<>(timeInHours, minY));
        }
        else if (randomPickVariance < 0.7)
        {
            double minY = prevY + ((y - prevY) * min) - 6 + (Math.random() * 12);
            minuteDataSeries.getData().add(new XYChart.Data<>(timeInHours, minY));
        } else if (randomPickVariance < 0.95)
        {
            double minY = prevY + ((y - prevY) * min) - 10 + (Math.random() * 20);
            minuteDataSeries.getData().add(new XYChart.Data<>(timeInHours, minY));
        }
        else
        {
            double minY = prevY + ((y - prevY) * min) - 15 + (Math.random() * 30);
            minuteDataSeries.getData().add(new XYChart.Data<>(timeInHours, minY));
        }
        // after 25hours delete old data
        if (timeInHours > 25)
        {
            minuteDataSeries.getData().remove(0);
        }
    }

    void play()
    {
        animation.play();
    }


    void stop()
    {
        animation.pause();
    }
}
