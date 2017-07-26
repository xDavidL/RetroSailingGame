package seng302.team18.model;


import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

/**
 * A class which stores information about a boat.
 */

public class Boat extends AbstractBoat implements GeographicLocation {
    private PolarPattern polar = new AC35PolarPattern();
    private DoubleProperty speed;
    //Set to -1 initially to prevent null pointer problems
    private IntegerProperty boatLegNumber = new SimpleIntegerProperty(-1);
    private Integer id;
    private double heading;
    private Coordinate coordinate;
    private Coordinate destination;
    private IntegerProperty place;
    private Long timeTilNextMark;
    private Long timeSinceLastMark;
    private Long timeAtLastMark;
    private int status;
    private boolean isControlled;
    private boolean sailOut;


    /**
     * A constructor for the Boat class
     *
     * @param boatName  The name of the boat
     * @param shortName The name of the team the boat belongs to
     * @param id        The id of the boat
     */
    public Boat(String boatName, String shortName, int id) {
        super(id, boatName, shortName);
        speed = new SimpleDoubleProperty();
        place = new SimpleIntegerProperty(1);
        timeTilNextMark = 0L;
        timeSinceLastMark = 0L;
        timeAtLastMark = 0L;
        isControlled = true;
        sailOut = true; // Starts with luffing
    }


    @Override
    public BoatType getType() {
        return BoatType.YACHT;
    }


    /**
     * A getter for the heading of the boat
     *
     * @return The heading of the boat
     */
    public double getHeading() {
        return heading;
    }


    /**
     * A setter for the heading of the boat
     *
     * @param heading The value the boat heading will be set to
     */
    public void setHeading(double heading) {
        this.heading = heading;
    }


    /**
     * A getter for the speed of the boat
     *
     * @return The speed of the boat
     */
    public double getSpeed() {
        return speed.get();
    }


    /**
     * A setter for the speed of the boat
     *
     * @param speed the speed of the boat
     */
    public void setSpeed(double speed) {
        this.speed.setValue(speed);
    }


    public DoubleProperty speedProperty() {
        return speed;
    }


    public int getLegNumber() {
        return boatLegNumber.get();
    }


    public IntegerProperty legNumberProperty() {
        return boatLegNumber;
    }


    public void setLegNumber(int boatLegNumber) {
        this.boatLegNumber.set(boatLegNumber);
    }


    public Coordinate getCoordinate() {
        return coordinate;
    }


    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }


    public Coordinate getDestination() {
        return destination;
    }


    public void setDestination(Coordinate destination) {
        this.destination = destination;
    }


    public int getPlace() {
        return place.get();
    }


    public void setPlace(int place) {
        this.place.set(place);
    }


    public IntegerProperty placeProperty() {
        return place;
    }


    public long getTimeTilNextMark() {
        return timeTilNextMark;
    }


    public void setTimeTilNextMark(long timeTilNextMark) {
        this.timeTilNextMark = timeTilNextMark;
    }


    public Long getTimeSinceLastMark() {
        return timeSinceLastMark;
    }


    public void setTimeSinceLastMark(long timeSinceLastMark) {
        this.timeSinceLastMark = timeSinceLastMark;
    }


    public Long getTimeAtLastMark() {
        return timeAtLastMark;
    }


    public void setTimeAtLastMark(Long timeAtLastMark) {
        this.timeAtLastMark = timeAtLastMark;
    }


    public boolean isSailOut() {
        return sailOut;
    }


    public void setSailOut(boolean sailOut) {
        this.sailOut = sailOut;
    }


    @Override
    public String toString() {
        return "Boat{" +
                "boatName=" + getName() +
                ", shortName='" + getShortName() + '\'' +
                ", speed=" + speed +
                ", leg=" + boatLegNumber +
                ", id=" + getId() +
                ", heading=" + heading +
                ", coordinate=" + coordinate +
                ", destination=" + destination +
                ", place=" + place +
                ", timeTilNextMark=" + timeTilNextMark +
                ", timeSinceLastMark=" + timeSinceLastMark +
                ", timeAtLastMark=" + timeAtLastMark +
                '}';
    }


    public int getStatus() {
        return status;
    }


    public void setStatus(int status) {
        this.status = status;
    }


    public boolean isControlled() {
        return isControlled;
    }


    public void setControlled(boolean controlled) {
        isControlled = controlled;
    }


    /**
     * Uses boats polar pattern to calculate boats TWS
     *
     * @param windSpeed   double, the speed of the wind
     * @param windHeading double, the direction of the wind
     * @return double, the tws of the boat
     */
    public double getBoatTWS(double windSpeed, double windHeading) {
        double twa = getTrueWindAngle(windHeading);
        return polar.getSpeedForBoat(twa, windSpeed);
    }


    /**
     * Gets true wind angle for boat.
     * NOTE: Expects wind heading as
     *
     * @param windHeading double, Heading of the wind
     * @return double, True wind angle for the boat.
     */
    public double getTrueWindAngle(double windHeading) {

        double flippedWindDirection = (windHeading + 180) % 360; // flipping wind direction
        double theta = 180 - flippedWindDirection;
        double boatPlusTheta = heading + theta;
//        double windPlusTheta = windHeading + theta; //will be 180

        if (boatPlusTheta > 360) {
            boatPlusTheta = boatPlusTheta - 360;
        }

        if (boatPlusTheta < 0) {
            boatPlusTheta = 360 + boatPlusTheta;
        }

        double trueWindAngle;

        if (boatPlusTheta > 180) {
            double angleFrom180 = boatPlusTheta - 180;
            trueWindAngle = 180 - angleFrom180;
        } else {
            trueWindAngle = boatPlusTheta;
        }

        return trueWindAngle;
    }


    /**
     * Sets heading so that VMG towards up wind is maximum and updates the speed.
     * <p>
     * Pre-condition: boat is heading towards from 0 to 180 degree.
     *
     * @param windSpeed     double, speed of the wind in knots
     * @param windDirection double, direction of the wind (degrees)
     */
    public void optimalUpWind(double windSpeed, double windDirection) {
        double left = 270;
        double right = 90;
        double windRelativeHeading = (heading - windDirection + 360) % 360;
        if (windRelativeHeading <= right) {
            double optimalAngle = (polar.upWindAngle(windSpeed) + windDirection) % 360;
            double optimalSpeed = polar.upWindSpeed(windSpeed);
            setHeading(optimalAngle);
            setSpeed(optimalSpeed);
        } else if (windRelativeHeading >= left) {
            double optimalAngle = (360 - polar.upWindAngle(windSpeed) + windDirection) % 360;
            double optimalSpeed = polar.upWindSpeed(windSpeed);
            setHeading(optimalAngle);
            setSpeed(optimalSpeed);
        }
    }


    /**
     * Sets heading so that VMG towards downwind is maximum and updates the speed.
     * <p>
     * Pre-condition: boat is heading towards from 90 to 270 degree.
     *
     * @param windSpeed     double, speed of the wind in knots
     * @param windDirection double, direction of the wind (degrees)
     */
    public void optimalDownWind(double windSpeed, double windDirection) {
        double right = 90;
        double left = 270;
        double bottom = 180;
        double windRelativeHeading = (heading - windDirection + 360) % 360;
        if (windRelativeHeading >= right && windRelativeHeading <= bottom) {
            double optimalAngle = (polar.downWindAngle(windSpeed) + windDirection) % 360;
            double optimalSpeed = polar.downWindSpeed(windSpeed);
            setHeading(optimalAngle);
            setSpeed(optimalSpeed);
        } else if (windRelativeHeading >= bottom && windRelativeHeading <= left) {
            double optimalAngle = (360 - polar.downWindAngle(windSpeed) + windDirection) % 360;
            double optimalSpeed = polar.downWindSpeed(windSpeed);
            setHeading(optimalAngle);
            setSpeed(optimalSpeed);
        }
    }

}
