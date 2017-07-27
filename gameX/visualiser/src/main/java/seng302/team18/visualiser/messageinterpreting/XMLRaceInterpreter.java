package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.*;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * A MessageInterpreter that takes a AC35XMLRaceMessage and updates the start time, participants, and course of a Race.
 */
public class XMLRaceInterpreter extends MessageInterpreter {

    private Race race;

    /**
     * Constructor for XMLRaceInterpreter. Takes a Race as a parameter which it updates every time a
     * AC35XMLBoatMessage is interpreted.
     * @param race to be updated.
     */
    public XMLRaceInterpreter(Race race) {
        this.race = race;
    }

    /**
     * Interprets the message of the XMLRaceMessage
     *
     * @param message to be interpreted
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35XMLRaceMessage) {
            AC35XMLRaceMessage raceMessage = (AC35XMLRaceMessage) message;
            final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssZ");
            ZonedDateTime start = ZonedDateTime.parse(raceMessage.getStartTime(), DATE_TIME_FORMATTER);
            race.setStartTime(start);

            race.setParticipantIds(new ArrayList<>(raceMessage.getParticipants().keySet()));

            Course course = race.getCourse();
            course.setMarkRoundings(raceMessage.getMarkRoundings());
            course.setCompoundMarks(raceMessage.getCompoundMarks());
            course.setBoundaries(raceMessage.getBoundaryMarks());
        }
    }

}