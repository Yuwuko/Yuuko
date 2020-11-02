package com.yuuko.core.commands;

import com.yuuko.core.MessageHandler;
import com.yuuko.core.events.entity.MessageEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;

public abstract class Command {
    private final String name;
    private final Module module;
    private final int minimumParameters;
    private final long cooldownDurationMilliseconds;
    private final HashMap<String, Long> cooldown;
    private final List<String> usage;
    private final List<Permission> permissions;
    private final boolean nsfw;

    protected static final Logger log = LoggerFactory.getLogger(Command.class);

    public Command(String name, Module module, int minimumParameters, long cooldownDurationMilliseconds, List<String> usage, boolean nsfw, List<Permission> permissions) {
        this.name = name;
        this.module = module;
        this.minimumParameters = minimumParameters;
        this.cooldownDurationMilliseconds = cooldownDurationMilliseconds;
        this.cooldown = new HashMap<>();
        this.usage = usage;
        this.nsfw = nsfw;
        this.permissions = permissions;
    }

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

    // Rate limiting only works in this context since commands are singleton objects.
    // If command objects were generated dynamically rate limiting would have to be handled externally.
    public boolean checkCooldown(MessageEvent e) {
        // Commands with no cooldown will be set to a duration of -1.
        if(cooldownDurationMilliseconds == -1) {
            return true;
        }

        final String guildId = e.getGuild().getId();
        if(cooldown.containsKey(guildId)) {
            long timeRemaining = cooldownDurationMilliseconds - (System.currentTimeMillis() - cooldown.get(guildId));

            if(timeRemaining > 0) {
                EmbedBuilder embed = new EmbedBuilder().setTitle("Cooldown").setDescription("Please wait " + timeRemaining + "ms before using the **" + e.getCommand().getName() + "** command again.");
                MessageHandler.sendMessage(e, embed.build());
                return false;
            } else {
                cooldown.replace(guildId, System.currentTimeMillis());
                return true;
            }
        } else {
            cooldown.put(guildId, System.currentTimeMillis());
            return true;
        }
    }

    // Abstract method signature to ensure method is implemented.
    public abstract void onCommand(MessageEvent e);
}
