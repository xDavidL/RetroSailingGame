package seng302.team18.data;

/**
 * Created by dhl25 on 11/04/17.
 */
public class AC35XMLRegattaMessage implements MessageBody {
    private double centralLat;
    private double centralLong;
    private String utcOffset;

    public AC35XMLRegattaMessage(double centralLat, double centralLong, String utcOffset) {
        this.centralLat = centralLat;
        this.centralLong = centralLong;
        this.utcOffset = utcOffset;
    }


    @Override
    public AC35MessageType getType() {
        return AC35MessageType.XML_REGATTA;
    }


    public double getCentralLat() {
        return centralLat;
    }


    public double getCentralLong() {
        return centralLong;
    }


    public String getUtcOffset() {
        return utcOffset;
    }
}