package seng302.team18.visualiser.sound;

import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import seng302.team18.visualiser.ClientRace;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.BooleanSupplier;

/**
 * Plays the start sound at the appropriate time
 */
public class StartSoundPlayer implements Runnable {

    private final ClientRace race;
    private final AudioPlayer audioPlayer;


    public StartSoundPlayer(ClientRace race, AudioPlayer audioPlayer) {
        this.race = race;
        this.audioPlayer = audioPlayer;
    }


    @Override
    public void run() {
        // Wait until we know when the start time is
        waitWhile(() -> race.getStartTime().toInstant().equals(Instant.EPOCH));

        // Wait until time to play start sound
        long durationMillis = ((Double) audioPlayer.getDuration(SoundEffect.RACE_START_LEAD_IN).toMillis()).longValue();
        ZonedDateTime playTime = race.getStartTime().minus(durationMillis, ChronoUnit.MILLIS);

        long millisUntilStart = ChronoUnit.MILLIS.between(race.getCurrentTime(), race.getStartTime());
        final long LEAD_IN_CUT_OFF = 1000;

        if (millisUntilStart >= LEAD_IN_CUT_OFF) {
            if (race.getCurrentTime().isBefore(playTime)) {
                waitWhile(() -> race.getCurrentTime().isBefore(playTime));

                audioPlayer.playEffect(SoundEffect.RACE_START_LEAD_IN);
            } else {
                long millisSincePlayStart = ChronoUnit.MILLIS.between(playTime, race.getCurrentTime());
                MediaPlayer player = audioPlayer.getEffectPlayer(SoundEffect.RACE_START_LEAD_IN);
                player.setStartTime(new Duration(millisSincePlayStart));
                player.setOnEndOfMedia(player::dispose);
                player.play();
            }

        }

        final long START_GUN_OFFSET = -750; // millis
        waitWhile(() -> race.getCurrentTime().isBefore(race.getStartTime().plus(START_GUN_OFFSET, ChronoUnit.MILLIS)));

        audioPlayer.playEffect(SoundEffect.RACE_START);
    }


    /**
     * Waits while the condition evaluates to true
     *
     * @param condition the condition
     */
    private void waitWhile(BooleanSupplier condition) {
        while (condition.getAsBoolean()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                // pass
            }
        }
    }
}
