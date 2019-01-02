package com.yuuko.core;

import com.google.api.services.youtube.model.SearchResult;
import com.yuuko.core.modules.Command;
import com.yuuko.core.modules.Module;
import com.yuuko.core.modules.audio.handlers.AudioManagerManager;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;
import org.discordbots.api.client.DiscordBotListAPI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class Cache {
    public static JDA JDA;
    public static User BOT;
    public static List<Command> COMMANDS;
    public static List<Module> MODULES;
    public static List<String> SETTINGS;
    public static DiscordBotListAPI BOT_LIST;
    public static LinkedList<String> LAST_THIRTEEN;
    public static String LATEST_INFO;
    public static String[] STANDARD_STRINGS;
    public static final HashMap<Long, List<SearchResult>> audioSearchResults = new HashMap<>();
    static AudioManagerManager AUDIO_MANAGER_MANAGER;
}
