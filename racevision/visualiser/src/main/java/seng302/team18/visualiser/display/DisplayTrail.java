package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polyline;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Course;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by spe76 on 27/04/17.
 */
public class DisplayTrail {
    private List<Coordinate> coordinates;
    private Polyline polyline;
    private String name;
    private Double heading;
    private Double headingVarience;
    private Integer length;


    public DisplayTrail(String name, Color color, double headingVariance, int length) {
        this.name = name;
        this.headingVarience = headingVariance;
        this.length = length;
        coordinates = new ArrayList<>();
        polyline = new Polyline();
        polyline.setStroke(color);
        heading = 1000000000d;
    }


    public void addPoint(Coordinate coordinate, double heading, PixelMapper pixelMapper) {
        int trailSize = polyline.getPoints().size();
        if (Math.abs(this.heading - heading) < headingVarience && trailSize >= 4) {
            polyline.getPoints().remove(trailSize - 1);
            polyline.getPoints().remove(trailSize - 2);
            coordinates.remove(coordinates.size() - 1);
        }
        this.heading = heading;
        XYPair pixel = pixelMapper.convertCoordPixel(coordinate);
        polyline.getPoints().addAll(pixel.getX(), pixel.getY());
        coordinates.add(coordinate);
//        if (coordinates.size() > length) {
//
//        }
    }

    public void reDraw(PixelMapper pixelMapper) {
        List<Double> updatedTrailPoints = new ArrayList<>();
        for (Coordinate coordinate : coordinates) {
            XYPair newPixels = pixelMapper.convertCoordPixel(coordinate);
            updatedTrailPoints.add(newPixels.getX());
            updatedTrailPoints.add(newPixels.getY());
        }
        polyline.getPoints().clear();
        polyline.getPoints().addAll(updatedTrailPoints);
    }

    public void addToGroup(Group group) {
        group.getChildren().add(polyline);
    }

}
