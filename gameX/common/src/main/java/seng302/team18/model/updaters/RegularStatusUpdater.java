package seng302.team18.model.updaters;

import seng302.team18.model.Boat;
import seng302.team18.model.BoatStatus;
import seng302.team18.model.Race;
import seng302.team18.model.RaceStatus;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Class for handling of switching the race into the correct status (warning, started, finished etc) at the
 * appropriate times.
 */
public class RegularStatusUpdater implements Updater {

    private ZonedDateTime warningTime;
    private ZonedDateTime prepTime;
    private ZonedDateTime startTime;


    /**
     * Construct a new RegularStatusUpdater.
     * Used for updating a normal race. Race is deemed finished when ALL players are in a finished state.
     *
     * @param initialTime Time of construction.
     * @param warningTimeSeconds until we switch to WARNING.
     * @param prepTimeSeconds until we switch from WARNING to PREPARATORY.
     * @param startTimeSeconds until we switch from PREPARATORY to STARTED.
     */
    public RegularStatusUpdater(ZonedDateTime initialTime, long warningTimeSeconds, long prepTimeSeconds, long startTimeSeconds) {
        this.warningTime = initialTime.plusSeconds(warningTimeSeconds);
        this.prepTime = initialTime.plusSeconds(warningTimeSeconds + prepTimeSeconds);
        this.startTime = initialTime.plusSeconds(warningTimeSeconds + prepTimeSeconds + startTimeSeconds);
    }


    /**
     * Updates the race status throughout the race.
     *
     * @param race to update.
     * @param time to update the race by.
     */
    @Override
    public void update(Race race, double time) {
        race.setStartTime(startTime);

        if (isFinished(race)) {
            race.setStatus(RaceStatus.FINISHED);
        } else if (ZonedDateTime.now().isAfter(race.getStartTime())) {
            race.setStatus(RaceStatus.STARTED);
            race.getStartingList().stream()
                    .filter(boat -> boat.getStatus().equals(BoatStatus.PRE_START))
                    .forEach(boat -> boat.setStatus(BoatStatus.RACING));
        } else if (ZonedDateTime.now().isAfter(prepTime)) {
            race.setStatus(RaceStatus.PREPARATORY);
        } else if (ZonedDateTime.now().isAfter(warningTime)) {
            race.setStatus(RaceStatus.WARNING);
        } else {
            race.setStatus(RaceStatus.PRESTART);
        }
    }


    /**
     * Checks if a regular race is finished by checking the status of each boat.
     *
     * @param race to check
     * @return if the race is finished.
     */
    private boolean isFinished(Race race) {
        Collection<BoatStatus> finishedStatuses = Arrays.asList(BoatStatus.DNF, BoatStatus.DNS, BoatStatus.FINISHED, BoatStatus.DSQ);
        List<Boat> startingList = race.getStartingList();
        int numFinished = (int) startingList
                .stream()
                .filter(boat -> finishedStatuses.contains(boat.getStatus()))
                .count();
        return startingList.size() == numFinished && startingList.size() != 0;
    }
}
