package com.yuuko.core.commands.setting;

import com.yuuko.core.commands.Command;
import com.yuuko.core.commands.Module;
import com.yuuko.core.commands.setting.commands.*;

import java.util.Map;

import static java.util.Map.entry;

public class SettingModule extends Module {
    private static final Map<String, Class<? extends Command>> commands = Map.ofEntries(
            entry("prefix", PrefixSetting.class),
            entry("starboard", StarboardSetting.class),
            entry("comlog", CommandLogSetting.class),
            entry("modlog", ModerationLogSetting.class),
            entry("djmode", DjModeSetting.class),
            entry("nowplaying", NowPlayingSetting.class),
            entry("delexecuted", DeleteExecutedSetting.class)
    );

    public SettingModule() {
        super("setting", false, commands);
    }

}
