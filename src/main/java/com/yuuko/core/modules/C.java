package com.yuuko.core.modules;

import com.yuuko.core.modules.audio.commands.*;
import com.yuuko.core.modules.core.commands.*;
import com.yuuko.core.modules.developer.commands.CommandAddServers;
import com.yuuko.core.modules.developer.commands.CommandReloadApi;
import com.yuuko.core.modules.developer.commands.CommandSetStatus;
import com.yuuko.core.modules.math.commands.CommandRoll;
import com.yuuko.core.modules.media.commands.CommandKitsu;
import com.yuuko.core.modules.media.commands.CommandOsu;
import com.yuuko.core.modules.moderation.commands.CommandBan;
import com.yuuko.core.modules.moderation.commands.CommandKick;
import com.yuuko.core.modules.moderation.commands.CommandMute;
import com.yuuko.core.modules.moderation.commands.CommandNuke;
import com.yuuko.core.modules.nsfw.commands.CommandEfukt;
import com.yuuko.core.modules.nsfw.commands.CommandNeko;
import com.yuuko.core.modules.utility.commands.CommandBind;
import com.yuuko.core.modules.utility.commands.CommandChannel;
import com.yuuko.core.modules.utility.commands.CommandServer;
import com.yuuko.core.modules.utility.commands.CommandUser;
import com.yuuko.core.modules.world.commands.CommandLineStatus;
import com.yuuko.core.modules.world.commands.CommandTesco;
import com.yuuko.core.modules.world.commands.CommandWeather;

public final class C {
    // Dev module commands.
    public static final Command SET_STATUS = new CommandSetStatus();
    public static final Command ADD_SERVERS = new CommandAddServers();
    public static final Command RELOAD_API = new CommandReloadApi();

    // Core module commands.
    public static final Command SETUP = new CommandSetup();
    public static final Command MODULE = new CommandModule();
    public static final Command MODULES = new CommandModules();
    public static final Command HELP = new CommandHelp();
    public static final Command ABOUT = new CommandAbout();
    public static final Command SETTINGS = new CommandSettings();

    // Moderation module commands.
    public static final Command NUKE = new CommandNuke();
    public static final Command KICK = new CommandKick();
    public static final Command BAN = new CommandBan();
    public static final Command MUTE = new CommandMute();

    // Math module commands.
    public static final Command ROLL = new CommandRoll();

    // Utility module commands.
    public static final Command USER = new CommandUser();
    public static final Command SERVER = new CommandServer();
    public static final Command CHANNEL = new CommandChannel();
    public static final Command BIND = new CommandBind();

    // Media module commands.
    public static final Command OSU = new CommandOsu();
    public static final Command KITSU = new CommandKitsu();

    // Music module commands.
    public static final Command PLAY = new CommandPlay();
    public static final Command STOP = new CommandStop();
    public static final Command SKIP = new CommandSkip();
    public static final Command PAUSE = new CommandPause();
    public static final Command CURRENT = new CommandCurrent();
    public static final Command SHUFFLE = new CommandShuffle();
    public static final Command QUEUE = new CommandQueue();
    public static final Command BACKGROUND = new CommandBackground();
    public static final Command LAST = new CommandLast();
    public static final Command REPEAT = new CommandRepeat();
    public static final Command SEARCH = new CommandSearch();
    public static final Command CLEAR = new CommandClear();

    // World module commands.
    public static final Command LINE_STATUS = new CommandLineStatus();
    public static final Command WEATHER = new CommandWeather();
    public static final Command TESCO = new CommandTesco();

    // NSFW module commands
    public static final Command EFUKT = new CommandEfukt();
    public static final Command NEKO = new CommandNeko();

    public C() {
    }

}
