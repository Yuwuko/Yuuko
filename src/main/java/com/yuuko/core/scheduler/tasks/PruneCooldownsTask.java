package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Config;
import com.yuuko.core.scheduler.Task;

public class PruneCooldownsTask implements Task {

    @Override
    public void run() {
        Config.COMMANDS.keySet().forEach(key -> Config.COMMANDS.get(key).clearCooldowns());
    }
}
