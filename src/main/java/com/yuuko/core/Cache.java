package com.yuuko.core;

import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.audio.handlers.AudioManagerManager;
import org.discordbots.api.client.DiscordBotListAPI;

import java.util.LinkedList;
import java.util.List;

public class Cache {
    public static long PING;
    public static int GUILD_COUNT;
    public static List<Command> COMMANDS;
    public static List<Module> MODULES;
    public static List<String> SETTINGS;
    public static DiscordBotListAPI BOT_LIST;
    public static LinkedList<String> LAST_TEN;
    public static String LATEST_INFO;
    public static String[] STANDARD_STRINGS;
    public static int MESSAGES_PROCESSED;
    public static int COMMANDS_PROCESSED;
    public static int REACTS_PROCESSED;
    static AudioManagerManager AUDIO_MANAGER_MANAGER;
    public static int DB_POOL_ACTIVE;
    public static int DB_POOL_IDLE;

    static void updatePing() {
        PING = Configuration.BOT.getPing();
    }
}
