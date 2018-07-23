package com.basketbandit.core.modules;

import com.basketbandit.core.modules.audio.ModuleAudio;
import com.basketbandit.core.modules.core.ModuleCore;
import com.basketbandit.core.modules.developer.ModuleDeveloper;
import com.basketbandit.core.modules.game.ModuleGame;
import com.basketbandit.core.modules.logging.ModuleLogging;
import com.basketbandit.core.modules.math.ModuleMath;
import com.basketbandit.core.modules.moderation.ModuleModeration;
import com.basketbandit.core.modules.nsfw.ModuleNSFW;
import com.basketbandit.core.modules.transport.ModuleTransport;
import com.basketbandit.core.modules.utility.ModuleUtility;

public final class M {
    public static final Module CORE = new ModuleCore();
    public static final Module DEVELOPER = new ModuleDeveloper();
    public static final Module LOGGING = new ModuleLogging();
    public static final Module MATH = new ModuleMath();
    public static final Module MODERATION = new ModuleModeration();
    public static final Module AUDIO = new ModuleAudio();
    public static final Module RUNESCAPE = new ModuleGame();
    public static final Module TRANSPORT = new ModuleTransport();
    public static final Module UTILITY = new ModuleUtility();
    public static final Module NSFW = new ModuleNSFW();
}
