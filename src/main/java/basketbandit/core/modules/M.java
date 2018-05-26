package basketbandit.core.modules;

import basketbandit.core.modules.audio.ModuleAudio;
import basketbandit.core.modules.core.ModuleCore;
import basketbandit.core.modules.custom.ModuleCustom;
import basketbandit.core.modules.developer.ModuleDeveloper;
import basketbandit.core.modules.fun.ModuleFun;
import basketbandit.core.modules.game.ModuleGame;
import basketbandit.core.modules.logging.ModuleLogging;
import basketbandit.core.modules.math.ModuleMath;
import basketbandit.core.modules.moderation.ModuleModeration;
import basketbandit.core.modules.transport.ModuleTransport;
import basketbandit.core.modules.utility.ModuleUtility;

public final class M  {
    public static final Module CORE = new ModuleCore();
    public static final Module CUSTOM = new ModuleCustom();
    public static final Module DEVELOPER = new ModuleDeveloper();
    public static final Module FUN = new ModuleFun();
    public static final Module LOGGING = new ModuleLogging();
    public static final Module MATH = new ModuleMath();
    public static final Module MODERATION = new ModuleModeration();
    public static final Module MUSIC = new ModuleAudio();
    public static final Module RUNESCAPE = new ModuleGame();
    public static final Module TRANSPORT = new ModuleTransport();
    public static final Module UTILITY = new ModuleUtility();
}
