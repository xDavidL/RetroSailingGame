package seng302.team18.visualiser.display;

import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import seng302.team18.model.Coordinate;
import seng302.team18.util.XYPair;
import seng302.team18.visualiser.util.PixelMapper;

/**
 * Created by cslaven on 6/07/17.
 */
public class DisplaySail extends DisplayBoatDecorator {

    private Line sail;
    private PixelMapper pixelMapper;
    private Coordinate start;
    private final Rotate rotation = new Rotate(0, 0, 0);
    private final Scale zoom = new Scale(1, 1, 0, 0);
    private final double LENGTH = 15;

    /**
     * Creates a new instance of DisplayBoat
     *
     * @param boat the display boat being decorated
     */
    public DisplaySail(DisplayBoat boat, PixelMapper mapper) {
        super(boat);
        this.pixelMapper = mapper;
        sail = new Line();
        sail.setStartX(0.0);
        sail.setStartY(0.0);
        sail.setEndY(100);
        sail.setEndX(100);
        sail.getTransforms().addAll(rotation);
    }

    public void setCoordinate(Coordinate coordinate) {
        XYPair pixels = pixelMapper.coordToPixel(coordinate);
        sail.setLayoutX(pixels.getX());
        sail.setLayoutY(pixels.getY());
        super.setCoordinate(coordinate);
    }

    public void setScale(double scaleFactor) {
        zoom.setX(scaleFactor);
        zoom.setY(scaleFactor);
        super.setScale(scaleFactor);
    }

    public void addToGroup(Group group){
        group.getChildren().add(sail);
        sail.toFront();
        super.addToGroup(group);
    }


    public void setHeading(double heading) {
        rotation.setAngle(heading + 90);
        super.setHeading(heading);
    }

}
