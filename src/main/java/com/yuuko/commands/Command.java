package com.yuuko.commands;

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

    private final String name;
    private Module module;
    private final int minimumParameters;
    private final long cooldownDurationMilliseconds;
    private final HashMap<String, Long> cooldownsList;
    private final List<String> usage;
    private final List<Permission> permissions;
    private final boolean nsfw;
    private boolean enabled = true;

    public Command(String name, int minimumParameters, long cooldownDurationMilliseconds, List<String> usage, boolean nsfw, List<Permission> permissions, boolean... commandEnabled) {
        this.name = name;
        this.minimumParameters = minimumParameters;
        this.cooldownDurationMilliseconds = cooldownDurationMilliseconds;
        this.cooldownsList = new HashMap<>();
        this.usage = usage;
        this.nsfw = nsfw;
        this.permissions = permissions;

        if(commandEnabled.length > 0) {
            enabled = commandEnabled[0];
            if(!enabled) {
                log.warn("{} command has been disabled due to missing API information.", name.toUpperCase());
            }
        }
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

    public int getMinimumParameters() {
        return minimumParameters;
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
    public void setEnabled(boolean state) {
        enabled = state;
        log.warn("{} command has been {}.", name, state ? "enabled" : "disabled");
    }

    // Rate limiting only works in this context since commands are singleton objects.
    // If command objects were generated dynamically rate limiting would have to be handled externally.
    public boolean isCooling(MessageEvent e) {
        // Commands with no cooldown will be set to a duration of -1.
        if(cooldownDurationMilliseconds == -1) {
            return true;
        }

        final String guildId = e.getGuild().getId();
        if(cooldownsList.containsKey(guildId)) {
            long timeRemaining = cooldownDurationMilliseconds - (System.currentTimeMillis() - cooldownsList.get(guildId));

            if(timeRemaining > 0) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Cooldown").setDescription("Please wait " + timeRemaining + "ms before using the **" + e.getCommand().getName() + "** command again.");
                MessageDispatcher.reply(e, embed.build());
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
            long timeRemaining = cooldownDurationMilliseconds - (System.currentTimeMillis() - cooldownsList.get(key));
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
    public abstract void onCommand(MessageEvent e) throws Exception;
}
