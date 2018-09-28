package com.basketbandit.core;

import com.basketbandit.core.modules.Command;
import com.basketbandit.core.modules.Module;

import java.util.List;

public class SystemInformation {

    private static int guildCount;
    private static int userCount;
    private static List<Command> commandList;
    private static List<Module> moduleList;

    public static String getGuildCount() {
        return guildCount + "";
    }

    public static String getUserCount() {
        return userCount + "";
    }

    public static String getCommandCount() {
        return commandList.size() + "";
    }

    public static String getModuleCount() {
        return moduleList.size() + "";
    }

    public static List<Command> getCommandList() {
        return commandList;
    }

    public static List<Module> getModuleList() {
        return moduleList;
    }

    public static void incrementGuildCount(int guildCount) {
        SystemInformation.guildCount =+ guildCount;
    }

    static void setGuildCount(int guildCount) {
        SystemInformation.guildCount = guildCount;
    }

    public static void setUserCount(int userCount) {
        SystemInformation.userCount = userCount;
    }

    static void setCommandList(List<Command> commandList) {
        SystemInformation.commandList = commandList;
    }

    static void setModuleList(List<Module> moduleList) {
        SystemInformation.moduleList = moduleList;
    }

}

