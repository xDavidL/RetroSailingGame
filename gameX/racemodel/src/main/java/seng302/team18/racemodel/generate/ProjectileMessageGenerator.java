package seng302.team18.racemodel.generate;

import seng302.team18.model.Projectile;
import seng302.team18.util.ByteCheck;
import seng302.team18.util.SpeedConverter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Generates projectile messages
 */
public class ProjectileMessageGenerator extends ScheduledMessageGenerator{

    private Projectile projectile;

    public ProjectileMessageGenerator(int type, Projectile projectile) {
        super(120, type);
        this.projectile = projectile;
    }

    @Override
    protected byte[] getPayload() throws IOException {
        final double BYTE_COORDINATE_TO_DOUBLE = 180.0 / 2147483648.0;
        final double BYTE_HEADING_TO_DOUBLE = 360.0 / 65536.0;

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        byte[] sourceID = ByteCheck.intToByteArray(projectile.getId());
        Double latitude = (projectile.getLocation().getLatitude() / BYTE_COORDINATE_TO_DOUBLE);
        int latInt = latitude.intValue();
        Double longitude = (projectile.getLocation().getLongitude() / BYTE_COORDINATE_TO_DOUBLE);
        int longInt = longitude.intValue();
        byte[] latitudeBytes = ByteCheck.intToByteArray(latInt);
        byte[] longitudeBytes = ByteCheck.intToByteArray(longInt);
        Double heading = (projectile.getHeading() / BYTE_HEADING_TO_DOUBLE);
        short headingShort = heading.shortValue();
        byte[] headingBytes = ByteCheck.shortToByteArray(headingShort);
        byte[] speed = ByteCheck.intToByteArray(new SpeedConverter().knotsToMms(projectile.getSpeed()).intValue());

        outStream.write(sourceID);
        outStream.write(latitudeBytes);
        outStream.write(longitudeBytes);
        outStream.write(headingBytes);
        outStream.write(speed);

        return outStream.toByteArray();
    }

    public int getProjectileId() {
        return projectile.getId();
    }
}
