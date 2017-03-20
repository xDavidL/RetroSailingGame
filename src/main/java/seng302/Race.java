package seng302;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A class to represent an individual race.
 */
public class Race {

    private ArrayList<Boat> startingList = new ArrayList<>();
    private Course course;
    private ArrayList<Boat> finishedList = new ArrayList<>();


    /**
     * Race class constructor.
     *
     * @param startingList Arraylist holding all entered boats
     * @param course       Course object
     */
    public Race(ArrayList<Boat> startingList, Course course) {
        this.startingList = startingList;
        this.course = course;
        setCourseForBoats();
    }

    public void setStartingCoordintes() {
        for (Boat b : startingList) {
            Coordinate start1 = course.getCompoundMarks().get(0).getMarks().get(0).getMarkCoordinates();
            Coordinate start2 = course.getCompoundMarks().get(0).getMarks().get(1).getMarkCoordinates();
            Coordinate midPoint = GPSCalculations.GPSMidpoint(start1, start2);
            b.setCoordinate(midPoint);
        }
    }


    /**
     * Called in Race constructor.
     * Set up the course CompoundMarks for each boat in the race as well as set the
     * current(starting CompoundMark) and next CompoundMark.
     */
    private void setCourseForBoats() {
        for (Boat boat : startingList) {
//            boat.setCompoundMarkList(course.getCompoundMarks());
//            boat.setCurrentCompoundMark(boat.getCompoundMarkList().get(0));
//            boat.setNextCompoundMark(boat.getCompoundMarkList().get(1));
            boat.setCurrentLeg(course.getLegs().get(0));
            boat.setNextLeg(course.getLegs().get(1));
            System.out.println(course.getLegs().get(0).getHeading());
        }
    }

    /**
     * Starting list getter.
     *
     * @return Arraylist holding all entered boats
     */
    public ArrayList<Boat> getStartingList() {
        return startingList;
    }


    /**
     * Starting list setter.
     *
     * @param startingList Arraylist holding all entered boats
     */
    public void setStartingList(ArrayList<Boat> startingList) {
        this.startingList = startingList;
    }


    /**
     * Course getter.
     *
     * @return Course object
     */
    public Course getCourse() {
        return course;
    }


    /**
     * Course setter.
     *
     * @param course Course object
     */
    public void setCourse(Course course) {
        this.course = course;
    }


    /**
     * Convert a value given in knots to meters per second.
     *
     * @param knots speed in knots.
     * @return speed in meters per second.
     */
    public static double knotsToMetersPerSecond(double knots) {
        return ((knots * 1.852) / 3.6);
    }


    public void updateBoats(double time) { // time in seconds
        for (Boat boat : startingList) {
            if (!(boat.isFinished())) {
                updateBoat(boat, time);
                boat.setHeading(GPSCalculations.retrieveHeading(boat.getCoordinate(),
                        boat.getCurrentLeg().getDestination().getMidCoordinate()));
            }
        }
    }


    private void updateBoat(Boat boat, double time) {
        System.out.println(boat.getHeading());

        if (boat.getBoatName().equals("Emirates")) {
            System.out.printf("Boats dist: %.2f | Mark dist: %.2f\n", boat.getDistanceTravelled(), boat.getCurrentLeg().getDistance());
        }
        if ((Math.abs(boat.getCurrentLeg().getDestination().getMidCoordinate().getLongitude() - boat.getCoordinate().getLongitude())
                < 0.0001)
                && (Math.abs(boat.getCurrentLeg().getDestination().getMidCoordinate().getLatitude() - boat.getCoordinate().getLatitude())
                < 0.0001)) {
            if (boat.getCurrentLeg().getLegNumber() < (course.getLegs().size() - 1)) {
                boat.setNextLeg(course.getLegs().get(boat.getCurrentLeg().getLegNumber() + 1)); // set  next leg if current leg is not the last
            }
            boat.setCurrentLeg(boat.getNextLeg());
            boat.setHeading(GPSCalculations.retrieveHeading(boat.getCoordinate(),
                    boat.getCurrentLeg().getDestination().getMidCoordinate()));

        }
        double speed = boat.getSpeed() * 1000 / 3600; // meters per second
        double distanceTravelled = speed * time; // meters
        Coordinate nextCoordinate = GPSCalculations.coordinateToCoordinate(boat.getCoordinate(), boat.getHeading(), distanceTravelled);
        boat.setCoordinate(nextCoordinate);

    }
}