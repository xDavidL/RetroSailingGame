package seng302.display;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.util.Duration;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * Class to display time from a specific time-zone on a JavaFX Label.
 */
public class ZoneTimeClock extends AnimationTimer {

    private Label timerLabel;
    private double timeScaleFactor;
    private ZonedDateTime zonedDateTime;
    private ZoneId zoneId;
    private long previousTime = 0;
    private final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public ZoneTimeClock(Label timerLabel, double timeScale, ZoneId zoneId) {
        this.timerLabel = timerLabel;
        this.timeScaleFactor = timeScale;

        this.zoneId = zoneId;
        timerLabel.setTextFill(Color.BLACK);
        timerLabel.setStyle("-fx-font-size: 2em;");
    }

    @Override
    public void handle(long currentTime) {
        if (previousTime == 0) {
            previousTime = currentTime;
            zonedDateTime = ZonedDateTime.now(zoneId);
            return;
        }

        double nanoSecondsElapsed = (currentTime - previousTime) * timeScaleFactor;
        previousTime = currentTime;

        zonedDateTime = zonedDateTime.plusNanos((long) nanoSecondsElapsed);
        timerLabel.setText(zonedDateTime.format(timeFormatter));
    }


}
