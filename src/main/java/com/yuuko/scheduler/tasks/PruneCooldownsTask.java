package com.yuuko.scheduler.tasks;

import com.yuuko.Yuuko;
import com.yuuko.commands.Command;
import com.yuuko.scheduler.Task;

public class PruneCooldownsTask implements Task {

    @Override
    public void run() {
        Yuuko.COMMANDS.values().forEach(Command::pruneCooldowns);
    }
}
