package basketbandit.core.modules.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.playback.AudioFrame;
import net.dv8tion.jda.core.audio.AudioSendHandler;

public class AudioPlayerSendHandler implements AudioSendHandler {

    private final AudioPlayer audioPlayer;
    private AudioFrame lastFrame;

    /**
     * @param audioPlayer Audio player to wrap.
     */
    AudioPlayerSendHandler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
    }

    /**
     * Returns if it can provide the last frame.
     * @return boolean.
     */
    @Override
    public boolean canProvide() {
        if(lastFrame == null) {
            lastFrame = audioPlayer.provide();
        }

        return lastFrame != null;
    }

    /**
     * Provides 20ms of audio.
     * @return audio data.
     */
    @Override
    public byte[] provide20MsAudio() {
        if(lastFrame == null) {
            lastFrame = audioPlayer.provide();
        }

        byte[] data = lastFrame != null ? lastFrame.data : null;
        lastFrame = null;

        return data;
    }

    /**
     * Returns if is opus. (always is)
     * @return true
     */
    @Override
    public boolean isOpus() {
        return true;
    }

}