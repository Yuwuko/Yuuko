package com.yuuko.modules;

import com.yuuko.MessageDispatcher;
import com.yuuko.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Command {
    protected static final Logger log = LoggerFactory.getLogger(Command.class);
    private Module module;
    private boolean enabled = true;
    private final String name;
    private final int parameters;
    private final long cooldownDuration; // milliseconds
    private final HashMap<String, Long> cooldownsList = new HashMap<>();
    private final List<String> usage;
    private final List<Permission> permissions;
    private final boolean nsfw;

    public Command(String name, List<String> usage) {
        this(name, usage, null, false, 0, -1L);
    }

    public Command(String name, List<String> usage, boolean nsfw) {
        this(name, usage, null, nsfw, 0, -1L);
    }

    public Command(String name, List<String> usage, int parameters) {
        this(name, usage, null, false, parameters, -1L);
    }

    public Command(String name, List<String> usage, boolean nsfw, int parameters) {
        this(name, usage, null, nsfw, parameters, -1L);
    }

    public Command(String name, List<String> usage, List<Permission> permissions) {
        this(name, usage, permissions, false, 0, -1L);
    }

    public Command(String name, List<String> usage, List<Permission> permissions, int parameters) {
        this(name, usage, permissions, false, parameters, -1L);
    }

    public Command(String name, List<String> usage, List<Permission> permissions, int parameters, long cooldownDuration) {
        this(name, usage, permissions, false, parameters, cooldownDuration);
    }

    public Command(String name, List<String> usage, List<Permission> permissions, boolean nsfw, int parameters, long cooldownDuration) {
        this.name = name;
        this.usage = usage;
        this.permissions = permissions;
        this.nsfw = nsfw;
        this.parameters = parameters;
        this.cooldownDuration = cooldownDuration;
    }

    void setModule(Module module) {
        this.module = module;
    } // only used during reflection process at startup

    public String getName() {
        return name;
    }

    public Module getModule() {
        return module;
    }

    public int getParameters() {
        return parameters;
    }

    public List<String> getUsage() {
        return usage;
    }

    public boolean isNSFW() {
        return nsfw || module.isNSFW();
    }

    public List<Permission> getPermissions() {
        return permissions;
    }

    public boolean isEnabled() {
        return enabled;
    }

    // allows commands to be toggled off globally.
    protected void setEnabled(boolean state) {
        enabled = state;
        log.warn("{} command has been {}.", name, state ? "enabled" : "disabled");
    }

    // Rate limiting only works in this context since commands are singleton objects.
    // If command objects were generated dynamically rate limiting would have to be handled externally.
    public boolean isCooling(MessageEvent context) {
        // Commands with no cooldown will be set to a duration of -1.
        if(cooldownDuration == -1) {
            return true;
        }

        final String guildId = context.getGuild().getId();
        if(cooldownsList.containsKey(guildId)) {
            long timeRemaining = cooldownDuration - (System.currentTimeMillis() - cooldownsList.get(guildId));

            if(timeRemaining > 0) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Cooldown").setDescription("Please wait " + timeRemaining + "ms before using the **" + context.getCommand().getName() + "** command again.");
                MessageDispatcher.reply(context, embed.build());
                return false;
            } else {
                cooldownsList.replace(guildId, System.currentTimeMillis());
                return true;
            }
        } else {
            cooldownsList.put(guildId, System.currentTimeMillis());
            return true;
        }
    }

    // I want a method to be able to purge lists on demand - reducing memory usage in a predictable way.
    public void pruneCooldowns() {
        HashMap<String, Long> cooldownsTempList = new HashMap<>();
        cooldownsList.keySet().forEach(key -> {
            long timeRemaining = cooldownDuration - (System.currentTimeMillis() - cooldownsList.get(key));
            if(timeRemaining > 0) {
                cooldownsTempList.put(key, cooldownsList.get(key));
            }
        });
        cooldownsList.clear();
        cooldownsList.putAll(cooldownsTempList);
    }

    /**
     * Get a random number using the given bound
     * @param bound int
     * @return int
     */
    public int getRandom(int bound) {
        return ThreadLocalRandom.current().nextInt(bound);
    }

    // Abstract method signature to ensure method is implemented.
    public abstract void onCommand(MessageEvent context) throws Exception;
}
