package seng302.team18.visualiser.messageinterpreting;

import seng302.team18.interpreting.MessageInterpreter;
import seng302.team18.message.AcceptanceMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.model.Race;

/**
 * The MarkLocationInterpreter that sets the client's player id
 *
 * @see MessageInterpreter
 */
public class AcceptanceInterpreter extends MessageInterpreter {

    private Race race;


    public AcceptanceInterpreter(Race race) {
        this.race = race;
    }


    @Override
    public void interpret(MessageBody message) {
        if (message instanceof AcceptanceMessage) {
            int sourceId = ((AcceptanceMessage) message).getSourceId();
            race.setPlayerId(sourceId);
        }
    }

}