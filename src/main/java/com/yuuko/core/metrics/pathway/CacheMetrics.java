package com.yuuko.core.metrics.pathway;

import com.yuuko.core.commands.audio.handlers.YouTubeSearchHandler;

import java.util.concurrent.atomic.AtomicInteger;

public class CacheMetrics {
    public final AtomicInteger TRACK_ID_CACHE_HITS = new AtomicInteger();
    public final AtomicInteger TRACK_ID_CACHE_SIZE = new AtomicInteger();

    public void update() {
        TRACK_ID_CACHE_SIZE.set(YouTubeSearchHandler.getSearchCache().size());
    }
}
