package seng302.team18.visualiser.sound;

import com.sun.istack.internal.NotNull;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Manages the playback of audio effects
 */
public class AudioPlayer {

    private final Map<SoundEffect, AudioClip> effectClips;
    private final Map<SoundEffect, MediaPlayer> effectPlayers;
    private MediaPlayer musicPlayer;
    private final Map<Ambient, MediaPlayer> ambientPlayers;

    private static final double RELATIVE_VOLUME_MUSIC = 0.6;
    private static final double RELATIVE_VOLUME_EFFECT = 1.0;
    private static final double RELATIVE_VOLUME_AMBIENT = 0.175;

    @SuppressWarnings("FieldCanBeLocal")
    private double volumeMusic = 1;
    @SuppressWarnings("FieldCanBeLocal")
    private double volumeEffect = 1;
    @SuppressWarnings("FieldCanBeLocal")
    private double volumeAmbient = 1;

    private boolean isMutedMusic = false;
    private boolean isMutedEffect = false;
    private boolean isMutedAmbient = false;

    /**
     * Constructs a new instance of AudioPlayer
     */
    public AudioPlayer() {
        effectPlayers = Arrays.stream(SoundEffect.values())
                .collect(Collectors.toMap(effect -> effect, effect -> new MediaPlayer(new Media(effect.getUrl()))));
        effectClips = Arrays.stream(SoundEffect.values())
                .collect(Collectors.toMap(effect -> effect, effect -> new AudioClip(effect.getUrl())));
        ambientPlayers = new HashMap<>();
    }


    /**
     * Plays the specified sound effect. Calling while the sound is still playing will result in two instances of the
     * sound playing and audible at once.
     *
     * @param effect the sound effect to play.
     */
    public void playEffect(SoundEffect effect) {
        effectClips.get(effect).play(getVolumeEffect());
    }


    /**
     * Gets the duration of the specified sound effect. Returns {@link Duration#UNKNOWN Duration.UNKOWN}
     * if the media player is not loaded yet
     *
     * @param effect the effect
     * @return the duration of the effect
     */
    public Duration getDuration(SoundEffect effect) {
        if (!effectPlayers.get(effect).getStatus().equals(MediaPlayer.Status.READY)) {
            return Duration.UNKNOWN;
        }

        return effectPlayers.get(effect).getMedia().getDuration();
    }


    /**
     * Creates a new MediaPlayer for a given effect.
     *
     * @param effect the effect
     * @return a new instance of MediaPlayer loaded with this effect
     */
    public MediaPlayer getEffectPlayer(SoundEffect effect) {
        // Uses the one from the map so that it is more likely to be in a ready state.
        MediaPlayer player = effectPlayers.get(effect);
        effectPlayers.put(effect, new MediaPlayer(new Media(effect.getUrl())));
        player.setVolume(getVolumeEffect());
        return player;
    }


    public void loopMusic(Music track) {
        if (musicPlayer != null) {
            musicPlayer.stop();
            musicPlayer.dispose();
            musicPlayer = null;
        }

        if (track != null) {
            musicPlayer = new MediaPlayer(new Media(track.getUrl()));
            musicPlayer.setVolume(getVolumeMusic());
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.play();
        }
    }


    public void loopAmbient(@NotNull Ambient track) {
        if (!ambientPlayers.containsKey(track)) {
            MediaPlayer player = new MediaPlayer(new Media(track.getUrl()));
            player.setVolume(getVolumeAmbient());
            player.play();
            player.setCycleCount(MediaPlayer.INDEFINITE);
            ambientPlayers.put(track, player);
        }
    }


    public void stopAllAmbient() {
        Set<Map.Entry<Ambient, MediaPlayer>> players = ambientPlayers.entrySet();
        players.forEach(entry -> {
            MediaPlayer player = entry.getValue();
            player.stop();
            player.dispose();
        });
        ambientPlayers.clear();
    }


    private double getVolumeMusic() {
        return (isMutedMusic) ? 0 : volumeMusic * RELATIVE_VOLUME_MUSIC;
    }


    private double getVolumeEffect() {
        return (isMutedEffect) ? 0 : volumeEffect * RELATIVE_VOLUME_EFFECT;
    }


    private double getVolumeAmbient() {
        return (isMutedAmbient) ? 0 : volumeAmbient * RELATIVE_VOLUME_AMBIENT;
    }
}
