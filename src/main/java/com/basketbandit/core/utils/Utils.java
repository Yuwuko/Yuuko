package com.basketbandit.core.utils;

import com.basketbandit.core.SystemClock;
import com.basketbandit.core.SystemInformation;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.GenericMessageEvent;
import net.dv8tion.jda.core.managers.GuildController;
import org.discordbots.api.client.DiscordBotListAPI;
import org.xhtmlrenderer.swing.Java2DRenderer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class Utils {

    public static User botUser;
    public static DiscordBotListAPI botList;
    public static LinkedList<String> lastTen;
    public static String latestInfo;
    public static String[] standardStrings;
    private static int messagesProcessed;
    private static int commandsProcessed;
    private static int reactsProcessed;


    /**
     * Returns a username and discriminator in format username#discriminator.
     * @param member the member to retrieve
     * @return username#discriminator
     */
    public static String getTag(Member member) {
        return getTag(member.getUser());
    }

    /**
     * Returns a username and discriminator in format username#discriminator.
     * @param user the user to retrieve
     * @return username#discriminator
     */
    private static String getTag(User user) {
        return user.getName() + "#" + user.getDiscriminator();
    }

    /**
     * Sends a message, saving those precious bytes.
     * @param event GenericMessageEvent
     * @param message String
     */
    public static void sendMessage(GenericMessageEvent event, String message) {
        try {
            event.getTextChannel().sendMessage(message).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends an embedded message.
     * @param event GenericMessageEvent
     * @param message MessageEmbed
     */
    public static void sendMessage(GenericMessageEvent event, MessageEmbed message) {
        try {
            event.getTextChannel().sendMessage(message).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends a message via message channel.
     * @param channel MessageChannel
     * @param message String
     */
    public static void sendMessage(MessageChannel channel, String message) {
        try {
            channel.sendMessage(message).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends an embedded message via message channel.
     * @param channel MessageChannel
     * @param message MessageEmbed
     */
    public static void sendMessage(MessageChannel channel, MessageEmbed message) {
        try {
            channel.sendMessage(message).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends an embedded message.
     * @param event GenericMessageEvent
     * @param file File
     */
    public static void sendMessage(GenericMessageEvent event, File file) {
        try {
            event.getTextChannel().sendFile(file).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends a file via message channel.
     * @param channel MessageChannel
     * @param file File
     */
    public static void sendMessage(MessageChannel channel, File file) {
        try {
            channel.sendFile(file).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends a file via message channel.
     * @param event GenericMessageEvent
     * @param bytes byte[]
     * @param fileName String
     */
    public static void sendMessage(GenericMessageEvent event, byte[] bytes, String fileName) {
        try {
            event.getChannel().sendFile(bytes, fileName).queue();
        } catch(Exception ex) {
            //
        }
    }

    /**
     * Sends a file via message channel.
     * @param channel MessageChannel
     * @param bytes byte[]
     * @param fileName String
     */
    public static void sendMessage(MessageChannel channel, byte[] bytes, String fileName) {
        try {
            channel.sendFile(bytes, fileName).queue();
        } catch(Exception ex) {
            //
        }
    }

    public static void sendException(Exception ex, String command) {
        try {
            MessageChannel channel = botUser.getJDA().getTextChannelById(495602825355591700L);
            channel.sendMessage(command + "\n`" + ex + "`").queue();
        } catch(Exception exc) {
            //
        }
    }

    /**
     * Replaces the last occurrence of a pattern with nothing.
     * @param stringBuilder StringBuilder
     * @param pattern String
     * @return StringBuilder
     */
    public static StringBuilder removeLastOccurrence(StringBuilder stringBuilder, String pattern) {
        int index = stringBuilder.lastIndexOf(pattern);
        if(index > -1) {
            stringBuilder.replace(index, index + 1, "");
        }

        return stringBuilder;
    }

    /**
     * Replaces the last occurrence of a pattern with nothing.
     * @param stringBuffer StringBuilder
     * @param pattern String
     * @return StringBuilder
     */
    public static StringBuffer removeLastOccurrence(StringBuffer stringBuffer, String pattern) {
        int index = stringBuffer.lastIndexOf(pattern);
        if(index > -1) {
            stringBuffer.replace(index, index + 1, "");
        }

        return stringBuffer;
    }

    /**
     * Extracts the module name from a class path.
     * @param string String
     * @param shortened boolean
     * @return String
     */
    public static String extractModuleName(String string, boolean shortened, boolean lowercase) {
        String returnString;

        if(shortened) {
            returnString = string.substring(string.lastIndexOf(".") + 7);
        } else {
            returnString = string.substring(string.lastIndexOf(".") + 1);
        }

        if(lowercase) {
            return returnString.toLowerCase();
        } else {
            return returnString;
        }
    }

    /**
     * Creates the muted role to correctly mute people.
     * @param guild Guild
     * @return Role
     */
    public static Role setupMutedRole(Guild guild) {
        GuildController controller = guild.getController();
        List<TextChannel> channels = guild.getTextChannels();
        List<Role> roleList = guild.getRoles();
        Role muted = null;

        for(Role role: roleList) {
            if(role.getName().equals("Muted")) {
                muted = role;
                break;
            }
        }

        if(muted == null) {
            muted = controller.createRole().setName("Muted").setPermissions(Permission.MESSAGE_READ, Permission.MESSAGE_HISTORY, Permission.VOICE_CONNECT).complete();

            for(TextChannel channel: channels) {
                channel.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD).complete();
            }
        }

        for(TextChannel channel: channels) {
            PermissionOverride override = null;

            for(PermissionOverride channelOverride: channel.getRolePermissionOverrides()) {
                if(channelOverride.getRole().equals(muted)) {
                    override = channelOverride;
                    break;
                }
            }

            if(override == null) {
                channel.createPermissionOverride(muted).setDeny(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION, Permission.VOICE_SPEAK, Permission.VOICE_USE_VAD).complete();
            }
        }

        return muted;
    }

    /**
     * Updates stats for DiscordBotList
     */
    public static void updateDiscordBotList() {
        try {
            JDA jda = botUser.getJDA();
            botList.setStats(jda.getShardInfo().getShardId(), jda.getShardInfo().getShardTotal(), Math.toIntExact(jda.getGuildCache().size()));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Take a xHTML input, renders and return an image based on it.
     * @param path String
     * @param html String
     * @param width int
     * @param height int
     * @param temp boolean
     * @return File rendered by method.
     */
    public static File xhtml2image(String path, String html, int width, int height, boolean temp) {
        try {
            // Nano time used for unique name.
            long unique = System.nanoTime();

            // xHTML code dynamically written to a file (since Java2DRenderer doesn't take a string?)
            BufferedWriter writer = new BufferedWriter(new FileWriter(path + unique + ".xhtml"));
            writer.write(html);
            writer.close();

            File xhtml = new File(path + unique + ".xhtml");

            // Reads xHTML code from file, renders, buffers an image and then writes that to a file.
            // Subclasses 2DRenderer to allow transparent images and such.
            final java.awt.Color TRANSPARENT = new Color(255, 255, 255, 0);
            final Java2DRenderer renderer = new Java2DRenderer(xhtml, width, height) {
                @Override
                protected BufferedImage createBufferedImage(final int width, final int height) {
                    final BufferedImage image = org.xhtmlrenderer.util.ImageUtil.createCompatibleBufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
                    org.xhtmlrenderer.util.ImageUtil.clearImage(image, TRANSPARENT);
                    return image;
                }
            };

            BufferedImage img = renderer.getImage();
            ImageIO.write(img, "png", new File(path + unique + ".png"));

            File image = new File(path + unique + ".png");

            if(temp) {
                // Delete the files to save time later.
                xhtml.deleteOnExit();
                image.deleteOnExit();
            }

            return image;
        } catch(Exception ex) {
            return null;
        }
    }

    /**
     * Takes a path and encoding type and returns a string of the file.
     * @param path String
     * @param encoding Charset
     * @return String retrieved from file.
     */
    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

    /**
     * Console output message method.
     */
    public static void consoleOutput() {

        // Default shell size is 80x24, this output will allow each message to take up the whole screen
        // thus giving the illusion that the current page is changing rather than just being rewritten under.
        System.out.println();
        System.out.println("           ____            _        _   ____                  _ _ _ ");
        System.out.println("          | __ )  __ _ ___| | _____| |_| __ )  __ _ _ __   __| (_) |_");
        System.out.println("          |  _ \\ / _` / __| |/ / _ \\ __|  _ \\ / _` | '_ \\ / _` | | __|");
        System.out.println("          | |_) | (_| \\__ \\   <  __/ |_| |_) | (_| | | | | (_| | | |_");
        System.out.println("          |____/ \\__,_|___/_|\\_\\___|\\__|____/ \\__,_|_| |_|\\__,_|_|\\__|");
        System.out.println();
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[COMMANDS]━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃ " + lastTen.get(0));
        System.out.println("┃ " + lastTen.get(1));
        System.out.println("┃ " + lastTen.get(2));
        System.out.println("┃ " + lastTen.get(3));
        System.out.println("┃ " + lastTen.get(4));
        System.out.println("┃ " + lastTen.get(5));
        System.out.println("┃ " + lastTen.get(6));
        System.out.println("┃ " + lastTen.get(7));
        System.out.println("┃ " + lastTen.get(8));
        System.out.println("┃ " + lastTen.get(9));
        System.out.println("┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[INFO]━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫");
        System.out.println("┃ " + latestInfo);
        System.out.println("┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[STATISTICS]━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫");
        System.out.println("┃ Uptime: " + SystemClock.getRuntime() + ", Ping: " + SystemInformation.getPing() + ", Guilds: " + SystemInformation.getGuildCount() + ", Modules: " + SystemInformation.getModuleCount() + ", Commands: " + SystemInformation.getCommandCount());
        System.out.println("┃ Messages processed: " + messagesProcessed + ", Reacts processed: " + reactsProcessed + ", Commands processed: " + commandsProcessed);
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    /**
     * Returns one of the array of standard strings (saves having to change loads of message in embeds!)
     * @param string int
     * @return the chosen string.
     */
    public static String getStandardString(int string) {
        return standardStrings[string];
    }

    /**
     * Increments processed integer.
     * @param type int
     */
    public static void incrementEvent(int type) {
        if(type == 0) {
            messagesProcessed++;
        } else if(type == 1) {
            reactsProcessed++;
        } else if(type == 2) {
            commandsProcessed++;
        }
    }

    /**
     * Updates the latest [INFO] message or the latest command message.
     * @param latest String
     */
    public static void updateLatest(String latest) {
        if(latest.startsWith("[INFO]")) {
            latestInfo = latest;
        } else {
            lastTen.addFirst(latest);
            if(lastTen.size() > 10) {
                lastTen.removeLast();
            }
        }
    }

}
