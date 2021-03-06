package seng302.team18.parse;

import seng302.team18.message.AC35MessageType;
import seng302.team18.message.AC35XMLHead;
import seng302.team18.message.MessageHead;
import seng302.team18.util.ByteCheck;

/**
 * A parser which reads information from an XML stream and creates message objects representing header information.
 */
public class AC35XMLHeadParser implements MessageHeadParser {

    /**
     * Reads a byte array and associates the information read with an XML message head.
     *
     * @param header A list of bytes representing the header of an incoming message
     * @return A message head object holding information from the byte stream
     */
    @Override
    public MessageHead parse(byte[] header) {
        final int TYPE_INDEX = 9;
        final int LEN_INDEX = 12;
        final int LEN_LENGTH = 2;
        AC35MessageType type = AC35MessageType.from((int) header[TYPE_INDEX]);
        int len = ByteCheck.byteToInt(header, LEN_INDEX, LEN_LENGTH);

        return new AC35XMLHead(type, len);
    }

    @Override
    public int headerSize() {
        return 14;
    }
}
