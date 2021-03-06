package seng302.team18.visualiser.interpret.americascup;

import seng302.team18.interpret.MessageInterpreter;
import seng302.team18.message.AC35MarkRoundingMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Boat;
import seng302.team18.visualiser.ClientRace;

import java.util.List;

/**
 * The MarkRoundingInterpreter that sets the boat's time at last mark.
 *
 * @see MessageInterpreter
 */
public class MarkRoundingInterpreter extends MessageInterpreter {
    private ClientRace race;


    /**
     * Constructor for MarkRoundingInterpreter.
     *
     * @param race the race to be updated.
     */
    public MarkRoundingInterpreter(ClientRace race) {
        this.race = race;
    }


    /**
     * Interpret method for MarkRoundingInterpreter. Gets the boat's time at last mark passed from the message.
     *
     * @param message to be interpreted. Of type AC35MarkRoundingMessage.
     * @see AC35MarkRoundingMessage
     */
    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AC35MarkRoundingMessage) {
            AC35MarkRoundingMessage roundingMessage = (AC35MarkRoundingMessage) message;
            List<Boat> boats = race.getStartingList();
            for (Boat boat : boats) {
                if (boat.getId().equals(roundingMessage.getBoatId())) {
                    boat.setTimeAtLastMark(roundingMessage.getTime());
                }
            }
        }
    }
}
