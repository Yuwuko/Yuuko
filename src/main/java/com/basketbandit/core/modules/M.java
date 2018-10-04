package com.basketbandit.core.modules;

import com.basketbandit.core.modules.audio.ModuleAudio;
import com.basketbandit.core.modules.core.ModuleCore;
import com.basketbandit.core.modules.developer.ModuleDeveloper;
import com.basketbandit.core.modules.math.ModuleMath;
import com.basketbandit.core.modules.media.ModuleMedia;
import com.basketbandit.core.modules.moderation.ModuleModeration;
import com.basketbandit.core.modules.nsfw.ModuleNSFW;
import com.basketbandit.core.modules.utility.ModuleUtility;
import com.basketbandit.core.modules.world.ModuleWorld;

public final class M {
    public static final Module CORE = new ModuleCore(null, null);
    public static final Module DEVELOPER = new ModuleDeveloper(null, null);
    public static final Module MATH = new ModuleMath(null, null);
    public static final Module MODERATION = new ModuleModeration(null, null);
    public static final Module AUDIO = new ModuleAudio(null, null);
    public static final Module MEDIA = new ModuleMedia(null, null);
    public static final Module WORLD = new ModuleWorld(null, null);
    public static final Module UTILITY = new ModuleUtility(null, null);
    public static final Module NSFW = new ModuleNSFW(null, null);
}
