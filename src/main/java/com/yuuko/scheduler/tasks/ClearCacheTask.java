package com.yuuko.scheduler.tasks;

import com.yuuko.modules.audio.handlers.YouTubeSearchHandler;
import com.yuuko.scheduler.Task;

public class ClearCacheTask implements Task {

    @Override
    public void run() {
        YouTubeSearchHandler.clearSearchCache();
    }
}
