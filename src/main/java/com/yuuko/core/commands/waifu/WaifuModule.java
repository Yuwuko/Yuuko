package com.yuuko.core.commands.waifu;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.waifu.commands.MyWaifuCommand;
import com.yuuko.core.commands.waifu.commands.WaifuBuyCommand;
import com.yuuko.core.commands.waifu.commands.WaifuStatsCommand;

import java.util.Arrays;
import java.util.List;

public class WaifuModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new MyWaifuCommand(),
            new WaifuBuyCommand(),
            new WaifuStatsCommand()
    );

    public WaifuModule() {
        super("waifu", false, commands);
    }
}
