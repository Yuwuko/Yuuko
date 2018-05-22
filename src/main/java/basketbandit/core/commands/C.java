package basketbandit.core.commands;

public final class C {

    // Dev module commands.
    public static final Command DATABASE_SETUP = new CommandDatabaseSetup();
    public static final Command SET_STATUS = new CommandSetStatus();

    // Core module commands.
    public static final Command SETUP = new CommandSetup();
    public static final Command MODULE = new CommandModule();
    public static final Command MODULES = new CommandModules();
    public static final Command HELP = new CommandHelp();

    // Moderation module commands.
    public static final Command NUKE = new CommandNuke();
    public static final Command KICK = new CommandKick();
    public static final Command BAN = new CommandBan();
    public static final Command CREATE_CHANNEL = new CommandAddChannel();
    public static final Command DELETE_CHANNEL = new CommandDeleteChannel();

    // Math module commands.
    public static final Command ROLL = new CommandRoll();
    public static final Command SUM = new CommandSum();

    // Utility module commands.
    public static final Command USER = new CommandUser();
    public static final Command SERVER = new CommandServer();

    // Fun module commands.
    public static final Command INSULT = new CommandInsult();
    public static final Command OVERREACT = new CommandOverreact();

    // RuneScape module commands.
    public static final Command RUNESCAPE_STATS = new CommandRuneScapeStats();

    // Custom module commands.
    public static final Command CUSTOM = new CommandCustom();
    public static final Command ADD_CUSTOM = new CommandAddCustom();
    public static final Command DELETE_CUSTOM = new CommandDeleteCustom();

    // Music module commands.
    public static final Command PLAY = new CommandPlay();
    public static final Command STOP = new CommandStop();
    public static final Command SKIP = new CommandSkip();
    public static final Command PAUSE = new CommandPause();
    public static final Command TRACK = new CommandTrack();
    public static final Command SHUFFLE = new CommandShuffle();
    public static final Command QUEUE = new CommandQueue();
    public static final Command SET_BACKGROUND = new CommandSetBackground();
    public static final Command UNSET_BACKGROUND = new CommandUnsetBackground();
    public static final Command LAST_TRACK = new CommandLastTrack();
    public static final Command TOGGLE_REPEAT = new CommandToggleRepeat();

    // Transport module commands.
    public static final Command LINE_STATUS = new CommandLineStatus();

}
