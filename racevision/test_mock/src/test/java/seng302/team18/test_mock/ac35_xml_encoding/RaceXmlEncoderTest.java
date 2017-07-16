package seng302.team18.test_mock.ac35_xml_encoding;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import seng302.team18.message.AC35RaceXMLComponents;
import seng302.team18.message.AC35XMLRaceMessage;
import seng302.team18.model.*;

import javax.xml.transform.dom.DOMSource;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Test for RaceXmlEncoder class.
 */
public class RaceXmlEncoderTest {

    private RaceXmlEncoder raceXmlEncoder = new RaceXmlEncoder();
    private AC35XMLRaceMessage raceMessage;
    private Element root;
    private Map<Integer, CompoundMark> encodedCompoundMarks = new HashMap<>();

    /**
     * Set up a AC35 standard race message.
     */
    private void setUpRaceMessage() {
        String startTime;
        List<Integer> participantIDs;
        List<CompoundMark> compoundMarks;
        List<MarkRounding> markRoundings;
        List<BoundaryMark> boundaryMarks;
        int raceID;

        final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss-Z");
        startTime = ZonedDateTime.now().format(DATE_TIME_FORMATTER);

        participantIDs = new ArrayList<>();

        for (Integer i = 107; i < 109; i++) {
            participantIDs.add(i);
        }

        Mark mark1 = new Mark(1, new Coordinate(22, 21));
        Mark mark2 = new Mark(2, new Coordinate(32, 31));
        Mark mark3 = new Mark(3, new Coordinate(12, 11));
        Mark mark4 = new Mark(4, new Coordinate(52, 51));
        Mark mark5 = new Mark(5, new Coordinate(42, 41));
        Mark mark6 = new Mark(6, new Coordinate(52, 51));

        List<Mark> startMarks = new ArrayList<>();
        startMarks.add(mark1);
        startMarks.add(mark2);

        List<Mark> finishMarks = new ArrayList<>();
        finishMarks.add(mark5);
        finishMarks.add(mark6);

        List<Mark> marks1 = new ArrayList<>();
        marks1.add(mark3);

        List<Mark> marks2 = new ArrayList<>();
        marks2.add(mark4);

        CompoundMark compoundMark1 = new CompoundMark("StartLine", startMarks, 102);
        CompoundMark compoundMark2 = new CompoundMark("Mark1", marks1, 103);
        CompoundMark compoundMark3 = new CompoundMark("Mark2", marks2, 104);
        CompoundMark compoundMark4 = new CompoundMark("FinishLine", finishMarks, 105);

        compoundMarks = new ArrayList<>();
        compoundMarks.add(compoundMark1);
        compoundMarks.add(compoundMark2);
        compoundMarks.add(compoundMark3);
        compoundMarks.add(compoundMark4);

        MarkRounding markRounding1 = new MarkRounding(1, compoundMark1);
        MarkRounding markRounding2 = new MarkRounding(2, compoundMark2);
        MarkRounding markRounding3 = new MarkRounding(3, compoundMark3);
        MarkRounding markRounding4 = new MarkRounding(4, compoundMark4);

        markRoundings = new ArrayList<>();
        markRoundings.add(markRounding1);
        markRoundings.add(markRounding2);
        markRoundings.add(markRounding3);
        markRoundings.add(markRounding4);

        BoundaryMark b1 = new BoundaryMark(1, new Coordinate(33, 34));
        BoundaryMark b2 = new BoundaryMark(2, new Coordinate(13, 14));
        BoundaryMark b3 = new BoundaryMark(3, new Coordinate(53, 54));
        BoundaryMark b4 = new BoundaryMark(4, new Coordinate(43, 44));

        boundaryMarks = new ArrayList<>();
        boundaryMarks.add(b1);
        boundaryMarks.add(b2);
        boundaryMarks.add(b3);
        boundaryMarks.add(b4);

        raceID = 123456;

        raceMessage = new AC35XMLRaceMessage();
        raceMessage.setRaceID(raceID);
        raceMessage.setBoundaryMarks(boundaryMarks);
        raceMessage.setCompoundMarks(compoundMarks);
        raceMessage.setMarkRoundings(markRoundings);
        raceMessage.setParticipantIDs(participantIDs);
        raceMessage.setStartTime(startTime);
    }


    /**
     * Get DOMSource from the RaceXmlEncoder in order to use it to run tests below.
     * @throws Exception parseConfigurationException
     */
    @Before
    public void setUp() throws Exception {
        setUpRaceMessage();
        DOMSource domSource = raceXmlEncoder.getDomSource(raceMessage);
        Document doc = (Document) domSource.getNode();
        doc.getDocumentElement().normalize();
        root = (Element) doc.getElementsByTagName(AC35RaceXMLComponents.ROOT_RACE.toString()).item(0);
    }


    /**
     * Test for encodeParticipants() method.
     */
    @Test
    public void encodeParticipantsTest() {
        List<Integer> encodedParticipants = new ArrayList<>();

        Node ids = root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_PARTICIPANTS.toString()).item(0);
        Element participants = (Element) ids;
        NodeList participantsNodeList = participants.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_YACHT.toString());

        for (int i = 0; i < participantsNodeList.getLength(); i++) {
            Node participant = participantsNodeList.item(i);
            Element participantElement = (Element) participant;
            encodedParticipants.add(Integer.parseInt(participantElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SOURCE_ID.toString())));
        }

        int firstParticipant = raceMessage.getParticipantIDs().get(0);
        int firstEncodedParticipant = encodedParticipants.get(0);
        assertEquals(firstEncodedParticipant, firstParticipant);
    }


    /**
     * Test for encodeCourse() method.
     * The encodeCourse method is built by encodeCompoundMark and encodeMarks methods, so these two methods are tested
     * as well.
     */
    @Test
    public void encodeCourseTest() {
        Node course = root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COURSE.toString()).item(0);
        Element courseElement = (Element) course;
        NodeList compoundMarkNodes = courseElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COMPOUND_MARK.toString());

        for (int i = 0; i < compoundMarkNodes.getLength(); i++) {
            Node compoundMarkNode = compoundMarkNodes.item(i);
            Element compoundMark = (Element) compoundMarkNode;

            List<Mark> encodedMarks = new ArrayList<>();
            NodeList markNodes = compoundMark.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_MARK.toString());

            for (int j = 0; j < markNodes.getLength(); j++) {
                Node markNode = markNodes.item(j);
                Element mark = (Element) markNode;
                int markId = Integer.parseInt(mark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SOURCE_ID.toString()));
                double markLat = Double.parseDouble(mark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_TARGET_LATITUDE.toString()));
                double markLng = Double.parseDouble(mark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_TARGET_LONGITUDE.toString()));
                encodedMarks.add(new Mark(markId, new Coordinate(markLat, markLng)));
            }

            String compoundMarkName = compoundMark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_NAME.toString());
            int compoundMarkID = Integer.parseInt(compoundMark.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_COMPOUND_MARK_ID.toString()));
            encodedCompoundMarks.put(compoundMarkID, new CompoundMark(compoundMarkName, encodedMarks, compoundMarkID));
        }

        CompoundMark firstCompoundMark = raceMessage.getCompoundMarks().get(0);
        CompoundMark firstEncodedCompoundMark = encodedCompoundMarks.get(102);
        assertEquals(firstCompoundMark.getMarks().get(0).getCoordinate(), firstEncodedCompoundMark.getMarks().get(0).getCoordinate());
    }


    /**
     * Test for encodeCompoundMarkSequence() method.
     * It is testing on markrounding.
     */
    @Test
    public void encodeCompoundMarkSequenceTest() {
        List<MarkRounding> encodedMarkRoundings = new ArrayList<>();

        Node markRounding = root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COMPOUND_MARK_SEQUENCE.toString()).item(0);
        Element markRoundingElement = (Element) markRounding;
        NodeList markRoundings = markRoundingElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_CORNER.toString());

        for (int i = 0; i < markRoundings.getLength(); i++) {
            Node markRoundingNode = markRoundings.item(i);
            Element markRoundingE = (Element) markRoundingNode;
            int seqID = Integer.parseInt(markRoundingE.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SEQUENCE_ID.toString()));
            int compoundMarkNum = Integer.parseInt(markRoundingE.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_COMPOUND_MARK_ID.toString()));
            encodedMarkRoundings.add(new MarkRounding(seqID, encodedCompoundMarks.get(compoundMarkNum)));
        }

        MarkRounding firstMarkRounding = raceMessage.getMarkRoundings().get(0);
        MarkRounding firstEncodedMarkRounding = encodedMarkRoundings.get(0);
        assertEquals(firstMarkRounding.getSequenceNumber(), firstEncodedMarkRounding.getSequenceNumber());
    }


    /**
     * Test for encodeCourseLimits() method.
     */
    @Test
    public void encodeCourseLimitsTest() {
        List<BoundaryMark> encodedBoundaryMarks = new ArrayList<>();

        Node boundaryNode = root.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_COURSE_BOUNDARIES.toString()).item(0);
        Element boundaryElement = (Element) boundaryNode;
        NodeList boundaryMarks = boundaryElement.getElementsByTagName(AC35RaceXMLComponents.ELEMENT_LIMIT.toString());

        for (int i = 0; i < boundaryMarks.getLength(); i++) {
            Node boundaryMarkNode = boundaryMarks.item(i);
            Element boundaryMarkElement = (Element) boundaryMarkNode;
            int seqId = Integer.parseInt(boundaryMarkElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_SEQUENCE_ID.toString()));
            double lat = Double.parseDouble(boundaryMarkElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_LATITUDE.toString()));
            double lon = Double.parseDouble(boundaryMarkElement.getAttribute(AC35RaceXMLComponents.ATTRIBUTE_LONGITUDE.toString()));
            encodedBoundaryMarks.add(new BoundaryMark(seqId, new Coordinate(lat, lon)));
        }

        BoundaryMark firstBoundaryMark = raceMessage.getBoundaryMarks().get(0);
        BoundaryMark firstEncodedBoundaryMark = encodedBoundaryMarks.get(0);
        assertEquals(firstBoundaryMark.getSequenceID(), firstEncodedBoundaryMark.getSequenceID());
    }
}