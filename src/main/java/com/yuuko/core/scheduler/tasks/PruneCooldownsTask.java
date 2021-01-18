package com.yuuko.core.scheduler.tasks;

import com.yuuko.core.Yuuko;
import com.yuuko.core.scheduler.Task;

public class PruneCooldownsTask implements Task {

    @Override
    public void run() {
        Yuuko.COMMANDS.keySet().forEach(key -> Yuuko.COMMANDS.get(key).pruneCooldowns());
    }
}
