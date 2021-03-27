package com.yuuko.scheduler.tasks;

import com.yuuko.modules.utility.commands.EventCommand;
import com.yuuko.scheduler.Task;

public class PruneAbandonedEventsTask implements Task {

    @Override
    public void run() {
        EventCommand.pruneEvents();
    }
}
