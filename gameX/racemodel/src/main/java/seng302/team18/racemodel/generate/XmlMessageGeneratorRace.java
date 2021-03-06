package seng302.team18.racemodel.generate;

import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.racemodel.encode.RaceXmlEncoder;
import seng302.team18.racemodel.encode.XmlEncoder;

/**
 * Generates a XMLRaceMessage
 */
public class XmlMessageGeneratorRace extends XMLMessageGenerator<AC35XMLRaceMessage> {

    public XmlMessageGeneratorRace(AC35XMLRaceMessage message) {
        super(message);
    }

    @Override
    protected XmlEncoder<AC35XMLRaceMessage> getEncoder() {
        return new RaceXmlEncoder();
    }
}
