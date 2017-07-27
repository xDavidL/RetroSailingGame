package seng302.team18.visualiser.util;

/**
 * Class to hold a data of a boats position as it statrs a leg
 */
public class SparklineDataPoint {

    private final String boatName;
    private final int boatPlace;
    private final String markPassedName;

    /**
     * Constructor, takes a boat and creates a spark line data point that holds
     * the boats name, place and leg number.
     *
     * @param boatName name of the boat
     * @param boatPlace position of the boat (1st, 2nd, 3rd...)
     * @param markPassedName the name of the mark passed
     */
    public SparklineDataPoint(String boatName, int boatPlace, String markPassedName) {
        this.boatName = boatName;
        this.boatPlace = boatPlace;
        this.markPassedName = markPassedName;
    }

    /**
     * returns the value of the boatName variable
     *
     * @return The boatName variable
     */
    public String getBoatName() {
        return boatName;
    }

    /**
     * returns the value of the boatPlace variable
     *
     * @return The boatPlace variable
     */
    public int getBoatPlace() {
        return boatPlace;
    }

    /**
     * returns the value of the markPassedNameboatPlace variable
     *
     * @return The markPassedName variable
     */
    public String getMarkPassedName() {
        return markPassedName;
    }
}
