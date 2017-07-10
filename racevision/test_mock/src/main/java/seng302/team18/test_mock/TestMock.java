package seng302.team18.test_mock;

import seng302.team18.model.Boat;
import seng302.team18.model.MarkRoundingEvent;
import seng302.team18.model.Race;
import seng302.team18.model.RaceStatus;
import seng302.team18.test_mock.connection.*;
import seng302.team18.test_mock.model.XmlMessageBuilder;

import java.time.ZonedDateTime;
import java.util.*;

import static java.lang.Thread.sleep;


/**
 * Class to handle a mock race
 */
public class TestMock implements Observer {

    private final Race race;
    private final Server server;
    private final XmlMessageBuilder xmlMessageBuilder;
    private List<Boat> boats = Arrays.asList(new Boat("Emirates Team New Zealand", "TEAM New Zealand", 121),
            new Boat("Oracle Team USA", "TEAM USA", 122),
            new Boat("Artemis Racing", "TEAM SWE", 123),
            new Boat("Groupama Team France", "TEAM France", 124),
            new Boat("Land Rover BAR", "TEAM Britain", 125),
            new Boat("Softbank Team Japan", "TEAM Japan", 126));



    public TestMock(Race race, Server server, XmlMessageBuilder messageBuilder) {
        this.race = race;
        this.server = server;
        this.xmlMessageBuilder = messageBuilder;
    }


    /**
     * The messages to be sent on a schedule during race simulation
     */
    private List<ScheduledMessageGenerator> scheduledMessages = new ArrayList<>();



    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof ClientConnection) {
            ClientConnection client = (ClientConnection) arg;
            race.addParticipant(boats.get(race.getStartingList().size())); // Maybe a bug
            sendXmlMessages(client);
        }
    }

    private void sendXmlMessages(ClientConnection newPlayer) {
        MessageGenerator generatorXmlRegatta = new XmlMessageGeneratorRegatta(xmlMessageBuilder.buildRegattaMessage(race));
        MessageGenerator generatorXmlRace = new XmlMessageGeneratorRace(xmlMessageBuilder.buildRaceXmlMessage(race));
        MessageGenerator generatorXmlBoats = new XmlMessageGeneratorBoats(xmlMessageBuilder.buildBoatsXmlMessage(race));

        newPlayer.sendMessage(generatorXmlRegatta.getMessage());
        server.broadcast(generatorXmlRace.getMessage());
        server.broadcast(generatorXmlBoats.getMessage());
    }


    /**
     * Simulate the race will sending the scheduled messages
     */
    public void runSimulation() {
        final int LOOP_FREQUENCY = 60;
        final int TIME_START = -5;
        final int TIME_WARNING = -3;
        final int TIME_PREP = -2;

        long timeCurr = System.currentTimeMillis();
        long timeLast;

        scheduledMessages.add(new RaceMessageGenerator(race));
        scheduledMessages.add(new HeartBeatMessageGenerator());

        // Set race time
        race.setStartTime(ZonedDateTime.now().minusMinutes(TIME_START));
        race.setStatus(RaceStatus.PRESTART);

        do {
            timeLast = timeCurr;
            timeCurr = System.currentTimeMillis();

            if ((race.getStatus() == RaceStatus.PRESTART) && ZonedDateTime.now().isAfter(race.getStartTime().plusMinutes(TIME_WARNING))) {
                race.setStatus(RaceStatus.WARNING);

            } else if ((race.getStatus() == RaceStatus.WARNING) && ZonedDateTime.now().isAfter((race.getStartTime().plusMinutes(TIME_PREP)))) {

                race.setStatus(RaceStatus.PREPARATORY);
                server.stopAcceptingConnections();

                for (Boat b : race.getStartingList()) {
                    scheduledMessages.add(new BoatMessageGenerator(b));
                }

            } else {
                race.updateBoats((timeCurr - timeLast));
                // Send mark rounding messages for all mark roundings that occured
                for (MarkRoundingEvent rounding : race.popMarkRoundingEvents()) {
                    server.broadcast((new MarkRoundingMessageGenerator(rounding, race.getId())).getMessage());
                }

                if ((race.getStatus() == RaceStatus.PREPARATORY) && ZonedDateTime.now().isAfter(race.getStartTime())) {
                    race.setStatus(RaceStatus.STARTED);
                }
            }

            // Send messages if needed
            for (ScheduledMessageGenerator sendable : scheduledMessages) {
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

        // Sends final message
        ScheduledMessageGenerator raceMessageGenerator = new RaceMessageGenerator(race);
        server.broadcast(raceMessageGenerator.getMessage());
    }
}
