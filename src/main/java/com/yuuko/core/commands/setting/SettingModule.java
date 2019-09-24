package com.yuuko.core.commands.setting;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.setting.commands.CommandLogSetting;
import com.yuuko.core.commands.setting.commands.ModerationLogSetting;
import com.yuuko.core.commands.setting.commands.PrefixSetting;
import com.yuuko.core.commands.setting.commands.StarboardSetting;

import java.util.Arrays;
import java.util.List;

public class SettingModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new PrefixSetting(),
            new StarboardSetting(),
            new CommandLogSetting(),
            new ModerationLogSetting()
    );

    public SettingModule() {
        super("setting", false, commands);
    }

}
