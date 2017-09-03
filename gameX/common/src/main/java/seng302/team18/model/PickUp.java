package seng302.team18.model;

import seng302.team18.message.PowerType;

/**
 * The physical power up object that can be picked up by a boat.
 */
public class PickUp {

    private BodyMass bodyMass;
    private PowerUp powerUp;
    private double timeout; // milliseconds
    private int id;

    public PickUp(int id) {
        this.id = id;
    }


    public Coordinate getLocation() {
        return bodyMass.getLocation();
    }


    public void setLocation(Coordinate location) {
        bodyMass.setLocation(location);
    }


    public BodyMass getBodyMass() {
        return bodyMass;
    }


    public void setBodyMass(BodyMass bodyMass) {
        this.bodyMass = bodyMass;
    }


    public void setPower(PowerUp powerUp) {
        this.powerUp = powerUp;
    }


    public PowerUp getPower() {
        return powerUp;
    }


    public double getTimeout() {
        return timeout;
    }


    public void setTimeout(double timeout) {
        this.timeout = timeout;
    }


    public double getRadius() {
        return bodyMass.getRadius();
    }


    public PowerType getType() {
        return powerUp.getType();
    }


    public double getPowerDuration() {
        return powerUp.getDuration();
    }


    public int getId() {
        return id;
    }
}
