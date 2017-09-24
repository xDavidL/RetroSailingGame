package seng302.team18.racemodel.builder.race;

import seng302.team18.model.*;
import seng302.team18.model.updaters.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builds a preset race for bumper boats game play
 * <p>
 * Concrete implementation of AbstractRaceBuilder.
 *
 * @see AbstractRaceBuilder
 */
public class BumperBoatsRaceBuilder extends AbstractRaceBuilder {
    private StatusUpdater statusUpdater  = null;

    @Override
    protected int getId() {
        return 11080703;
    }

    @Override
    protected RaceType getRaceType() {
        return RaceType.MATCH;
    }

    @Override
    protected List<Updater> getUpdaters() {
        List<Updater> updaters = new ArrayList<>();
        updaters.add(new BoatsUpdater());
        updaters.add(new BoatCollisionUpdater());
        updaters.add(new BumperBoatHealthUpdater());
        updaters.add(new BumperCourseShrinker(new Coordinate(5.00150, 4.0005), 200, 34, 0.0000025));
        updaters.add(new PowerUpUpdater(makePickUp(), 4));
        updaters.add(new ProjectileUpdater());
        updaters.add(new ProjectileHitUpdater());

        if (statusUpdater == null) {
            statusUpdater = new RegularStatusUpdater(ZonedDateTime.now(), 2, 1, 1);
        }
        updaters.add(statusUpdater);

        return updaters;
    }

    @Override
    protected RaceMode getRaceMode() {
        return RaceMode.BUMPER_BOATS;
    }


    @Override
    protected StartPositionSetter getPositionSetter() {
        return new CircularPositionSetter(100);
    }


    private PickUp makePickUp() {
        BodyMass mass = new BodyMass();
        mass.setRadius(12);
        mass.setWeight(0);

        PickUp pickUp = new PickUp(-1);
        pickUp.setBodyMass(mass);
        return pickUp;
    }
}
