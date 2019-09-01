package com.yuuko.core.commands.waifu.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;

public class MyWaifuCommand extends Command {

    public MyWaifuCommand() {
        super("mywaifu", Configuration.MODULES.get("waifu"), 0, Arrays.asList("-mywaifu"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
    }
}
