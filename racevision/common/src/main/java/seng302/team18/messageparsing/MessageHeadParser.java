package seng302.team18.messageparsing;

/**
 * Created by dhl25 on 10/04/17.
 */
public interface MessageHeadParser {


    public MessageHead parse(byte[] header);

    public int headerSize();


}