package seng302.team18.visualiser.display;

import javafx.animation.AnimationTimer;
import seng302.team18.visualiser.display.render.Renderable;
import seng302.team18.visualiser.display.ui.FPSReporter;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.ArrayList;
import java.util.List;


/**
 * An AnimationTimer to update the view of the race
 */
public class RaceLoop extends AnimationTimer {
    private long previousTime = 0;
    private double secondsElapsedSinceLastFpsUpdate = 0d;
    private int framesSinceLastFpsUpdate = 0;
    private FPSReporter fpsReporter;
    private PixelMapper pixelMapper;
    private List<Renderable> renderables = new ArrayList<>();


    /**
     * Constructor for the RaceLoop class.
     *
     * @param renderables thing that need to be rendered each frame
     * @param fpsReporter thing that updates fps
     * @param pixelMapper that maps
     */
    public RaceLoop(List<Renderable> renderables, FPSReporter fpsReporter, PixelMapper pixelMapper) {
        this.renderables.addAll(renderables);
        this.fpsReporter = fpsReporter;
        this.pixelMapper = pixelMapper;
    }


    @Override
    public void handle(long currentTime) {
        if (previousTime == 0) {
            previousTime = currentTime;
            return;
        }
        double secondsElapsed = (currentTime - previousTime) / 1e9f; // converting from nanoseconds to seconds
        previousTime = currentTime;
        updateFps(secondsElapsed);
        updateView();
    }


    /**
     * Call each renderer and update the display of the race.
     */
    private void updateView() {
        pixelMapper.calculateMappingScale();
        for (Renderable renderable : renderables) {
            renderable.render();
        }
    }


    /**
     * Update the FPS label showing the current frame rate.
     *
     * @param secondsElapsed The seconds elapsed since the last update.
     */
    private void updateFps(double secondsElapsed) {
        secondsElapsedSinceLastFpsUpdate += secondsElapsed;
        framesSinceLastFpsUpdate++;
        if (secondsElapsedSinceLastFpsUpdate >= 0.5d) {
            double fps = framesSinceLastFpsUpdate / secondsElapsedSinceLastFpsUpdate;
            fpsReporter.report(fps);
            secondsElapsedSinceLastFpsUpdate = 0;
            framesSinceLastFpsUpdate = 0;
        }
    }


    @Override
    public void stop() {
        super.stop();
        previousTime = 0;
    }
}

