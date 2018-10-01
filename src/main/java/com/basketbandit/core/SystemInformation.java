package com.basketbandit.core;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;
import net.dv8tion.jda.core.JDA;

import java.util.List;

public class SystemInformation {

    private static JDA bot;
    private static long ping;
    private static int guildCount;
    private static int userCount;
    private static List<Command> commands;
    private static List<Module> modules;
    private static List<String> settings;

    public static String getGuildCount() {
        return guildCount + "";
    }

    public static String getUserCount() {
        return userCount + "";
    }

    public static String getCommandCount() {
        return commands.size() + "";
    }

    public static String getModuleCount() {
        return modules.size() + "";
    }

    public static List<Command> getCommandList() {
        return commands;
    }

    public static List<Module> getModuleList() {
        return modules;
    }

    public static List<String> getSettingsList() {
        return settings;
    }

    public static void incrementGuildCount(int guildCount) {
        SystemInformation.guildCount += guildCount;
    }

    static void setGuildCount(int guildCount) {
        SystemInformation.guildCount = guildCount;
    }

    public static void setUserCount(int userCount) {
        SystemInformation.userCount = userCount;
    }

    static void setCommandList(List<Command> commandList) {
        SystemInformation.commands = commandList;
    }

    static void setModuleList(List<Module> moduleList) {
        SystemInformation.modules = moduleList;
    }

    static void setSettingsList(List<String> settingsList) {
        SystemInformation.settings = settingsList;
    }

    static void updatePing() {
        ping = bot.getPing();
    }

    public static String getPing() {
        return ping + "";
    }

    static void setBot(JDA inputBot) {
        bot = inputBot;
    }

}
