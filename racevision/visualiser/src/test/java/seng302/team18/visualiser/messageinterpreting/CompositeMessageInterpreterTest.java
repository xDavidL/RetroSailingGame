package seng302.team18.visualiser.messageinterpreting;

import org.junit.Before;
import org.junit.Test;
import seng302.team18.messageparsing.AC35MessageType;
import seng302.team18.model.Race;

/**
 * Test class for CompositeMessageInterpreter.
 */
public class CompositeMessageInterpreterTest {
    private Race race;
    private MessageInterpreter messageInterpreter;
    private MessageInterpreter boatLocationInterpreter;
    private MessageInterpreter markRoundingInterpreter;
    private MessageInterpreter xmlRegattaInterpreter;

    @Before
    public void setUp() {
        race = new Race();
        messageInterpreter = new CompositeMessageInterpreter();
        boatLocationInterpreter = new BoatLocationInterpreter(race);
        markRoundingInterpreter = new MarkRoundingInterpreter(race);
        xmlRegattaInterpreter = new XMLRegattaInterpreter(race);
        messageInterpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), boatLocationInterpreter);
    }

    @Test
    public void add() throws Exception {
        messageInterpreter.add(AC35MessageType.BOAT_LOCATION.getCode(), markRoundingInterpreter);
        messageInterpreter.add(AC35MessageType.XML_REGATTA.getCode(), xmlRegattaInterpreter);
    }

    @Test
    public void remove() throws Exception {
        messageInterpreter.remove(AC35MessageType.BOAT_LOCATION.getCode(), boatLocationInterpreter);
    }

}