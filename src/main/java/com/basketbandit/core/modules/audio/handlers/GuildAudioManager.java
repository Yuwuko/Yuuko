package com.basketbandit.core.modules.audio.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class GuildAudioManager {

    public final AudioPlayer player;
    public final TrackScheduler scheduler;

    /**
     * Creates a player and a track scheduler.
     * Without this class, trying to get persistent players, schedules, ect,
     * over an instanced module based bot would be pretty difficult.
     * @param manager Audio player manager to use for creating the player.
     */
    public GuildAudioManager(AudioPlayerManager manager) {
        player = manager.createPlayer();
        scheduler = new TrackScheduler(player);
        player.addListener(scheduler);
    }

    /**
     * @return Wrapper around AudioPlayer to use it as an AudioSendHandler.
     */
    public AudioPlayerSendHandler getSendHandler() {
        return new AudioPlayerSendHandler(player);
    }

}
