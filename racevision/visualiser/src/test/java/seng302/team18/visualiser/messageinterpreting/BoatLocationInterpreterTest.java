package seng302.team18.visualiser.messageinterpreting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import seng302.team18.messageparsing.AC35BoatLocationMessage;
import seng302.team18.messageparsing.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.model.Coordinate;
import seng302.team18.model.Race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for BoatLocationInterpreter
 */
public class BoatLocationInterpreterTest {

    private Race race;
    private List<Boat> startingList;
    private BoatLocationInterpreter interpreter;
    private MessageBody message;

    private Double delta = 0.001;

    @Before
    public void setUp() {
        race = new Race();
        Boat boat1 = new Boat("Big Boat", "BB", 420);
        Boat boat2 = new Boat("Medium Boat", "MB", 100);
        Boat boat3 = new Boat("Small Boat", "SB", 69);
        startingList = new ArrayList<>(Arrays.asList(boat1, boat2, boat3));
        race.setParticipantIds(Arrays.asList(420, 100, 69));
        race.setStartingList(startingList);
        interpreter = new BoatLocationInterpreter(race);
    }

    @Test
    public void interpretBoatLocationTest1() {
        message = new AC35BoatLocationMessage(420, new Coordinate(20, 20), 100, 10);
        interpreter.interpret(message);
        Boat boatToCheck = race.getStartingList().get(0);
        Assert.assertEquals(10, boatToCheck.getSpeed(), delta);
        Assert.assertEquals(100, boatToCheck.getHeading(), delta);
        Assert.assertEquals(20, boatToCheck.getCoordinate().getLatitude(), delta);
        Assert.assertEquals(20, boatToCheck.getCoordinate().getLongitude(), delta);
    }

    @Test
    public void interpretBoatLocationTest2() {
        message = new AC35BoatLocationMessage(100, new Coordinate(-20, -20), 365, 100);
        interpreter.interpret(message);
        Boat boatToCheck = race.getStartingList().get(1);
        Assert.assertEquals(100, boatToCheck.getSpeed(), delta);
        Assert.assertEquals(365, boatToCheck.getHeading(), delta);
        Assert.assertEquals(-20, boatToCheck.getCoordinate().getLatitude(), delta);
        Assert.assertEquals(-20, boatToCheck.getCoordinate().getLongitude(), delta);
    }

    @Test
    public void interpretBoatLocationTest3() {
        message = new AC35BoatLocationMessage(69, new Coordinate(-1000, -1000), -10, -10);
        interpreter.interpret(message);
        Boat boatToCheck = race.getStartingList().get(2);
        Assert.assertEquals(-10, boatToCheck.getSpeed(), delta);
        Assert.assertEquals(-10, boatToCheck.getHeading(), delta);
        Assert.assertEquals(0, boatToCheck.getCoordinate().getLatitude(), delta);
        Assert.assertEquals(0, boatToCheck.getCoordinate().getLongitude(), delta);
    }
}