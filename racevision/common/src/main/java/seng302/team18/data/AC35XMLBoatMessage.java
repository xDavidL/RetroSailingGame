package seng302.team18.data;

import seng302.team18.model.Boat;

import java.util.List;

/**
 * Created by dhl25 on 11/04/17.
 */
public class AC35XMLBoatMessage implements MessageBody {

    private List<Boat> boats;

    @Override
    public int getType() {
        return AC35MessageType.XML_BOATS.getCode();
    }

    public List<Boat> getBoats() {
        return boats;
    }

    public void setBoats(List<Boat> boats) {
        this.boats = boats;
    }
}