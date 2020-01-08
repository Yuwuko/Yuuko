package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.commands.audio.handlers.YouTubeSearchHandler;
import com.yuuko.core.scheduler.Task;

public class ClearCacheTask implements Task {

    @Override
    public void run() {
        YouTubeSearchHandler.clearSearchCache();
    }
}
