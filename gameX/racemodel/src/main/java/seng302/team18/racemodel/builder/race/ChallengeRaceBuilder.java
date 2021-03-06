package seng302.team18.racemodel.builder.race;

import seng302.team18.model.*;
import seng302.team18.model.updaters.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * RaceBuilder for the challenge mode.
 */
public class ChallengeRaceBuilder extends AbstractRaceBuilder {
    StatusUpdater statusUpdater = null;


    @Override
    protected int getId() {
        return 11082705;
    }


    @Override
    protected RaceType getRaceType() {
        return RaceType.MATCH;
    }


    @Override
    protected List<Updater> getUpdaters() {
        List<Updater> updaters = new ArrayList<>();
        updaters.add(new BoatsUpdater());
        updaters.add(new MarkCollisionUpdater(20));
        updaters.add(new OutOfBoundsUpdater());
        updaters.add(new MarkRoundingUpdater());
        updaters.add(new SpeedUpdater(34, 0.00025, 1));
        updaters.add(new ChallengeCourseShrinker(new Coordinate(38.21748,-106.52344), 34, 0.00000075, 0.025));

        if (statusUpdater == null) {
            statusUpdater = new RegularStatusUpdater(ZonedDateTime.now(), 5, 1, 2);
        }

        updaters.add(statusUpdater);

        return updaters;
    }


    @Override
    protected RaceMode getRaceMode() {
            return RaceMode.CHALLENGE_MODE;
        }


    @Override
    protected StartPositionSetter getPositionSetter() {
        return new StartLineSetter(20);
    }

}
