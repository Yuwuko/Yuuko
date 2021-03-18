package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Yuuko;
import com.yuuko.core.commands.Command;
import com.yuuko.core.scheduler.Task;

public class PruneCooldownsTask implements Task {

    @Override
    public void run() {
        Yuuko.COMMANDS.values().forEach(Command::pruneCooldowns);
    }
}
