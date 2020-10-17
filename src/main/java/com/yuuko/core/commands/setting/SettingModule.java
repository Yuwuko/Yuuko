package com.yuuko.core.commands.setting;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.setting.commands.*;

import java.util.Arrays;
import java.util.List;

public class SettingModule extends Module {
    private static final List<Command> commands = Arrays.asList(
            new PrefixSetting(),
            new StarboardSetting(),
            new CommandLogSetting(),
            new ModerationLogSetting(),
            new DjModeSetting(),
            new NowPlayingSetting(),
            new DeleteExecutedSetting()
    );

    public SettingModule() {
        super("setting", false, commands);
    }

}
