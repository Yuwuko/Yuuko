package com.yuuko.scheduler.tasks;

import com.yuuko.scheduler.Task;
import net.dv8tion.jda.api.entities.Message;

public class MessageDeleteTask implements Task {
    private final Message message;

    public MessageDeleteTask(Message message) {
        this.message = message;
    }

    @Override
    public void run() {
        message.delete().queue();
    }
}