package com.yuuko.scheduler.tasks;

import com.yuuko.commands.utility.commands.EventCommand;
import com.yuuko.scheduler.Task;

public class EventNotificationTask implements Task {

    @Override
    public void run() {
        EventCommand.notifyEvents();
    }
}
