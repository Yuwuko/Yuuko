package com.yuuko.core.commands.waifu.commands;

import com.yuuko.core.Configuration;
import com.yuuko.core.commands.Command;
import com.yuuko.core.events.entity.MessageEvent;

import java.util.Arrays;

public class WaifuBuyCommand extends Command {

    public WaifuBuyCommand() {
        super("waifubuy", Configuration.MODULES.get("waifu"), 0, Arrays.asList("-waifubuy <name>", "-waifubuy <id>"), false, null);
    }

    @Override
    public void onCommand(MessageEvent e) {
    }
}
