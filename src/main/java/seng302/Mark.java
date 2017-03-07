package seng302;

import java.util.ArrayList;
import java.util.Collections;

/**
 *  A class that represents a mark on the race course
 */
public class Mark {

    private ArrayList<Boat> boats = new ArrayList<>();
    private String markName;
    private float markDistance;
    private float distanceFromStart;
    private int markPosition;
    private int markHeading;


    /**
     * Constructor for the Mark class
     * @param markName The name of the mark
     * @param markDistance The distance of the leg
     */
    Mark(String markName, float markDistance, float distanceFromStart, int markPosition, int markHeading) {
            this.markName = markName;
            this.markDistance = markDistance;
            this.distanceFromStart = distanceFromStart;
            this.markPosition = markPosition;
            this.markHeading = markHeading;
    }


    public int getMarkHeading() {
        return markHeading;
    }

    public void setMarkHeading(int markHeading) {
        this.markHeading = markHeading;
    }

    /**
     * Getter for the mark's position in the course.
     * If markPosition = 1, then it is the first mark.
     * @return the mark position.
     */
    int getMarkPosition() {
        return markPosition;
    }


    /**
     * Getter for the length of the leg ending at this mark
     * @return the length of the leg
     */
    float getmarkDistance() {
        return markDistance;
    }


    /**
     * Getter for the distance from the start to the end of the current leg.
     * Use to decide if a boat has passed the mark.
     * @return the distance from the start to the end of the leg.
     */
    float getDistanceFromStart() {
        return distanceFromStart;
    }

    /**
     * A getter to return the variable boats from the Mark
     * @return The list of boats for the mark
     */
    public ArrayList<Boat> getBoats() {
            return boats;
        }


    /**
     * Setter the boats at the mark
     * @param boats Arraylist of boats at the mark
     */
    void setBoats(ArrayList<Boat> boats) {
            this.boats = boats;
    }


    /**
     * Getter for the mark name
     * @return the name of the mark
     */
    String getMarkName() {
            return markName;
    }


    /**
     * A setter for the name of the mark
     * @param markName The name that the mark will  be set to
     */
    public void setMarkName(String markName) {
            this.markName = markName;
        }


    /**
     * A method which displays the order the boats passed through the mark
     */
    void displayBoatOrder(){
        System.out.println(markName);
        for(int i = 0; i < boats.size(); i++){
            System.out.println(i+1 + ". " + boats.get(i).getTeamName() + " " + boats.get(i).getBoatName());
        }
    }
}

