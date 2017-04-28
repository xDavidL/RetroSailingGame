package seng302.team18.messageparsing;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dhl25 on 10/04/17.
 */
public class AC35MessageParserFactory implements MessageParserFactory {

    private final Map<AC35MessageType, MessageBodyParser> parserMap = initialiseMap();

    @Override
    public MessageHeadParser makeHeadParser() {
        return new AC35MessageHeadParser();
    }

    @Override
    public MessageBodyParser makeBodyParser(int type) {
        AC35MessageType ac35Type = AC35MessageType.from(type);
        return parserMap.get(ac35Type);
    }

    @Override
    public MessageErrorDetector makeDetector() {
        return new CRCChecker(); // TODO JESS
    }

    private Map<AC35MessageType, MessageBodyParser> initialiseMap() {
        Map<AC35MessageType, MessageBodyParser> parserMap = new HashMap<>();
        parserMap.put(AC35MessageType.YACHT_EVENT, new AC35YachtEventParser());
        parserMap.put(AC35MessageType.BOAT_LOCATION, new AC35BoatLocationParser());
        parserMap.put(AC35MessageType.XML_MESSAGE, new AC35XMLMessageParser(new AC35XMLParserFactory()));
        parserMap.put(AC35MessageType.RACE_STATUS, new AC35RaceStatusParser());
        parserMap.put(AC35MessageType.MARK_ROUNDING, new AC35MarkRoundingParser());
        return parserMap;
    }
}