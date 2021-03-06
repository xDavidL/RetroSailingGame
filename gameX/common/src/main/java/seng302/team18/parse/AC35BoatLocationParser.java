package seng302.team18.parse;

import com.google.common.io.ByteStreams;
import seng302.team18.message.AC35BoatLocationMessage;
import seng302.team18.message.MessageBody;
import seng302.team18.util.ByteCheck;
import seng302.team18.model.Coordinate;
import seng302.team18.util.SpeedConverter;

import java.io.IOException;
import java.io.InputStream;

/**
 * A parser which reads information from a byte stream and creates message objects representing boat location information.
 */
public class AC35BoatLocationParser implements MessageBodyParser {

    /**
     * Reads a stream and associates the information read with a boat location message.
     *
     * @param stream holds information about the locations of participants in the race.
     * @return A location message holding location information about boats.
     */
    @Override
    public MessageBody parse(InputStream stream) {
        try {
            return parse(ByteStreams.toByteArray(stream));
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * Reads a byte array and associates the information read with a boat location message.
     *
     * @param bytes A list of bytes represent information about the locations of participants in the race.
     * @return A location message holding location information about boats.
     */
    @Override
    public MessageBody parse(byte[] bytes) { // more final declarations than actual code LOL
        final double BYTE_COORDINATE_TO_DOUBLE = 180.0 / 2147483648.0;
        final double BYTE_HEADING_TO_DOUBLE = 360.0 / 65536.0;
        final int SOURCE_ID_INDEX = 7;
        final int SOURCE_ID_LENGTH = 4;
        final int LAT_INDEX = 16;
        final int LAT_LENGTH = 4;
        final int LONG_INDEX = 20;
        final int LONG_LENGTH = 4;
        final int HEADING_INDEX = 28;
        final int HEADING_LENGTH = 2;
        final int SPEED_INDEX = 38;
        final int SPEED_LENGTH = 2;
        final int SAIL_INDEX = 50;
        final int SAIL_LENGTH = 2;
        final int LIVES_INDEX = 30;
        final int LIVES_LENGTH = 2;

        int sourceID = ByteCheck.byteToInt(bytes, SOURCE_ID_INDEX, SOURCE_ID_LENGTH);
        double lat = ByteCheck.byteToInt(bytes, LAT_INDEX, LAT_LENGTH) * BYTE_COORDINATE_TO_DOUBLE;
        double longitude = ByteCheck.byteToInt(bytes, LONG_INDEX, LONG_LENGTH) * BYTE_COORDINATE_TO_DOUBLE;
        double heading = ByteCheck.byteToInt(bytes, HEADING_INDEX, HEADING_LENGTH) * BYTE_HEADING_TO_DOUBLE;
        double speed = ByteCheck.byteToInt(bytes, SPEED_INDEX, SPEED_LENGTH);
        speed = new SpeedConverter().mmsToKnots(speed);
        boolean sail = sails(bytes, SAIL_INDEX, SAIL_LENGTH);
        int lives = ByteCheck.byteToInt(bytes, LIVES_INDEX, LIVES_LENGTH);

        return new AC35BoatLocationMessage(sourceID, new Coordinate(lat, longitude), heading, speed, sail, lives);
    }


    /**
     * Converts the sail field (former drift field) into a boolean variable
     *
     * @param bytes A list of bytes represent information about the locations of participants in the race.
     * @param sailIndex The index of where the sail field is.
     * @param sailLength The length of the sail field.
     * @return The boolean variable representing if the sail is in / out on a boat.
     */
    private boolean sails(byte[] bytes, int sailIndex, int sailLength) {
        return ByteCheck.byteToShort(bytes, sailIndex, sailLength) != 0;
    }

}
