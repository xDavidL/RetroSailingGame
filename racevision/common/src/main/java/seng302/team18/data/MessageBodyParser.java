package seng302.team18.data;

/**
 * Created by dhl25 on 10/04/17.
 */
public interface MessageBodyParser {

    public MessageBody parse(byte[] bytes);
}