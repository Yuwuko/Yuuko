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
            entry("commandlog", CommandLogSetting.class),
            entry("moderationlog", ModerationLogSetting.class),
            entry("djmode", DjModeSetting.class),
            entry("playnotifications", PlayNotificationsSetting.class),
            entry("cleanupcommands", CleanupCommandsSetting.class),
            entry("eventchannel", EventChannelSetting.class)
    );

    public SettingModule() {
        super("setting", false, commands);
    }

}
