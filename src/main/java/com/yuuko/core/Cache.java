package com.yuuko.core;

import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.audio.handlers.AudioManagerManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import org.discordbots.api.client.DiscordBotListAPI;

import java.util.LinkedList;
import java.util.List;

public class Cache {
    public static JDA JDA;
    public static User BOT;

    public static long PING;
    public static List<Command> COMMANDS;
    public static List<Module> MODULES;
    public static List<String> SETTINGS;
    public static DiscordBotListAPI BOT_LIST;
    public static LinkedList<String> LAST_TEN;
    public static String LATEST_INFO;
    public static String[] STANDARD_STRINGS;
    static AudioManagerManager AUDIO_MANAGER_MANAGER;


    static void updatePing() {
        PING = JDA.getPing();
    }
}
