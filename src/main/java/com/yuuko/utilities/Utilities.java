package com.yuuko.utilities;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public final class Utilities {
    /**
     * Returns YouTube image url or placeholder if it doesn't exist
     * @param track {@link AudioTrack}
     * @return string image url
     */
    public static String getAudioTrackImage(AudioTrack track) {
        String[] uri = track.getInfo().uri.split("=");
        return (uri.length > 1) ? "https://img.youtube.com/vi/" + uri[1] + "/1.jpg" : "https://i.imgur.com/bCNQlm6.jpg";
    }
}
