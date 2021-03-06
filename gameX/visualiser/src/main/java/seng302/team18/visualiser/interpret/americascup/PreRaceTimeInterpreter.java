package seng302.team18.visualiser.interpret.americascup;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35RaceStatusMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.visualiser.ClientRace;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * The interpreter class for setting the pre-race start time.
 */
public class PreRaceTimeInterpreter extends MessageInterpreter {

    private ClientRace race;


    /**
     * Constructor for PreRaceTimeInterpreter.
     *
     * @param race to be updated.
     */
    public PreRaceTimeInterpreter(ClientRace race) {
        this.race = race;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35RaceStatusMessage) {
            AC35RaceStatusMessage statusMessage = (AC35RaceStatusMessage) message;

            Instant instant = Instant.ofEpochMilli(statusMessage.getStartTime());
            ZonedDateTime startTime = ZonedDateTime.ofInstant(instant, race.getCourse().getTimeZone());
            race.setStartTime(startTime);

            Instant currentInstant = Instant.ofEpochMilli(statusMessage.getCurrentTime());
            race.setCurrentTime(ZonedDateTime.ofInstant(currentInstant, race.getCourse().getTimeZone()));
        }
    }
}
