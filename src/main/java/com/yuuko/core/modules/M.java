package com.yuuko.core.modules;

import com.yuuko.core.modules.audio.ModuleAudio;
import com.yuuko.core.modules.core.ModuleCore;
import com.yuuko.core.modules.developer.ModuleDeveloper;
import com.yuuko.core.modules.math.ModuleMath;
import com.yuuko.core.modules.media.ModuleMedia;
import com.yuuko.core.modules.moderation.ModuleModeration;
import com.yuuko.core.modules.nsfw.ModuleNSFW;
import com.yuuko.core.modules.profile.ModuleProfile;
import com.yuuko.core.modules.utility.ModuleUtility;
import com.yuuko.core.modules.world.ModuleWorld;

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
    public static final Module PROFILE = new ModuleProfile(null, null);
}
