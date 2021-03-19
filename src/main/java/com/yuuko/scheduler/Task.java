package com.yuuko.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public interface Task {
    Logger log = LoggerFactory.getLogger(Task.class);

    void run();
}
