package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Configuration;
import com.yuuko.core.scheduler.Task;

public class PruneCooldownsTask implements Task {

    @Override
    public void run() {
        Configuration.COMMANDS.keySet().forEach(key -> Configuration.COMMANDS.get(key).clearCooldowns());
    }
}
