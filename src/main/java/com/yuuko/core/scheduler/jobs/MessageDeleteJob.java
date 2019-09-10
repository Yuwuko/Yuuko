package com.yuuko.core.scheduler.jobs;

import com.yuuko.core.scheduler.Job;
import com.yuuko.core.scheduler.tasks.MessageDeleteTask;
import net.dv8tion.jda.api.entities.Message;

import java.util.concurrent.TimeUnit;

public class MessageDeleteJob extends Job {
    private final MessageDeleteTask messageDeleteTask;

    public MessageDeleteJob(Message message) {
        super(1, 0, TimeUnit.MINUTES);
        this.messageDeleteTask = new MessageDeleteTask(message);
    }

    @Override
    public void run() {
        handleTask(messageDeleteTask);
    }
}
