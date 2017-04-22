package seng302.team18.test_mock;

import seng302.team18.model.*;
import seng302.team18.test_mock.XMLparsers.*;
import seng302.team18.test_mock.connection.BoatMessageGenerator;
import seng302.team18.test_mock.connection.RaceMessageGenerator;
import seng302.team18.test_mock.connection.ScheduledMessage;
import seng302.team18.test_mock.connection.Server;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;


/**
 * Class to handle a mock race
 */
public class TestMock {

    private Regatta regatta;
    private Course course;
    private ActiveRace race;

    private AC35RegattaContainer ac35RegattaContainer;
    private AC35BoatsContainer ac35BoatsContainer;
    private AC35RaceContainer ac35RaceContainer;

    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessage> messages = new ArrayList<>();

    //TODO give real port
    private Server server = new Server(5005);

    /**
     * Generate the classes necessary to visualise a mock race.
     * These are: Regatta, Race, Course
     */
    private void generateClasses() {
        generateCourse();
        generateRace();
    }

    /**
     * Read each test xml file and fill the containers so classes can be made
     */
    private void readFiles() {
        AC35RegattaParser ac35RegattaParser = new AC35RegattaParser();
        ac35RegattaContainer = ac35RegattaParser.parse(this.getClass().getResourceAsStream("/AC35regatta.xml"));

        AC35BoatsParser ac35BoatsParser = new AC35BoatsParser();
        ac35BoatsContainer = ac35BoatsParser.parse(this.getClass().getResourceAsStream("/AC35boats.xml"));

        AC35RaceParser ac35RaceParser = new AC35RaceParser();
        ac35RaceContainer = ac35RaceParser.parse(this.getClass().getResourceAsStream("/AC35race.xml"));
    }

    public void run() {
        readFiles();
        generateClasses();

        server.openServer();

        initMessageGenerators();
        runSimulation();

        server.closeServer();
    }

    /**
     * Initialise the generators for scheduled messages
     */
    private void initMessageGenerators() {
        messages.add(new BoatMessageGenerator(race.getStartingList()));
    }


    /**
     * Simulate the race will sending the scheduled messages
     */
    private void runSimulation() {
        final int LOOP_FREQUENCY = 10;

        long timeCurr = System.currentTimeMillis();
        long timeLast;

        do {
            timeLast = timeCurr;
            timeCurr = System.currentTimeMillis();

            // Update simulation
            race.updateBoats((timeCurr - timeLast) * 1e3);

            // Send messages if needed
            for (ScheduledMessage sendable : messages) {
                if (sendable.isTimeToSend(timeCurr)) {
                    server.broadcast(sendable.getMessage());
                }
            }

            server.pruneConnections();

            // Sleep
            try {
                sleep(1000 / LOOP_FREQUENCY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } while (!race.isFinished());
    }

    private void generateCourse() {
        Map<Integer, CompoundMark> compoundMarkMap = ac35RaceContainer.getCompoundMarks();
        List<CompoundMark> compoundMarks = new ArrayList<>();

        for (CompoundMark compoundMark : compoundMarkMap.values()) {
            compoundMarks.add(compoundMark);
        }

        List<BoundaryMark> boundaryMarks = ac35RaceContainer.getBoundaryMark();
        double windDirection = 0;

        ZoneId zoneId;
        String utcOffset = ac35RegattaContainer.getuTcOffset();
        if (utcOffset.startsWith("+") || utcOffset.startsWith("-")) {
            zoneId = ZoneId.of("UTC" + utcOffset);
        } else {
            zoneId = ZoneId.of("UTC+" + utcOffset);
        }

        Coordinate central = new Coordinate(ac35RegattaContainer.getCentralLatitude(), ac35RegattaContainer.getCentralLongtitude());

        course = new Course(compoundMarks, boundaryMarks, windDirection, zoneId);
        course.setCentralCoordinate(central);
    }

    private void generateRace() {
        List<Boat> startingList = ac35BoatsContainer.getBoats();
        race = new ActiveRace(startingList, course);
    }
}