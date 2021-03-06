package seng302.team18.message;

import java.util.List;

/**
 * MessageBody that contains information about a race's status.
 * More specifically, current time of the race, race status, start time, wind direction, wind speed and boat statuses.
 */
public class AC35RaceStatusMessage implements MessageBody {

    private long currentTime;
    private int raceStatus;
    private long startTime;
    private double windDirection;
    private double windSpeed;
    private List<AC35BoatStatusMessage> boatStatus;


    /**
     * Constructor for AC35RaceStatusMessage.
     *
     * @param currentTime   of the race in Epoch milliseconds.
     * @param raceStatus    of the race.
     * @param startTime     of the race in Epoch milliseconds.
     * @param windDirection of the race.
     * @param windSpeed     of the race.
     * @param boatStatus    a list of the AC35BoatStatusMessages.
     */
    public AC35RaceStatusMessage(long currentTime, int raceStatus, long startTime,
                                 double windDirection, double windSpeed, List<AC35BoatStatusMessage> boatStatus) {
        this.currentTime = currentTime;
        this.raceStatus = raceStatus;
        this.startTime = startTime;
        this.windDirection = windDirection;
        this.windSpeed = windSpeed;
        this.boatStatus = boatStatus;
    }

    @Override
    public int getType() {
        return AC35MessageType.RACE_STATUS.getCode();
    }

    /**
     * Getter for the current time of the race.
     *
     * @return the current time of the race in Epoch milliseconds.
     */
    public long getCurrentTime() {
        return currentTime;
    }

    /**
     * Getter for the start time of the race.
     *
     * @return the start time of the race in Epoch milliseconds.
     */
    public long getStartTime() {
        return startTime;
    }

    /**
     * Getter for the wind direction of the race.
     *
     * @return the wind direction.
     */
    public double getWindDirection() {
        return windDirection;
    }

    /**
     * Getter for the wind speed of the race
     *
     * @return the current wind speed
     */
    public double getWindSpeed() {
        return windSpeed;
    }

    /**
     * Getter for the map of the boats id to the status, leg, and estimated time to next mark.
     *
     * @return the map of the boats id to the status, leg, and estimated time to next mark.
     */
    public List<AC35BoatStatusMessage> getBoatStatus() {
        return boatStatus;
    }

    /**
     * Getter for the current status of the race.
     *
     * @return the current status of the race.
     */
    public int getRaceStatus() {
        return raceStatus;
    }
}
