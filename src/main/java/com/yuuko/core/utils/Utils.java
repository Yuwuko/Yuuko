package com.yuuko.core.utils;

import com.yuuko.core.Cache;
import com.yuuko.core.Configuration;
import com.yuuko.core.SystemClock;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.managers.GuildController;
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
import java.util.List;

public final class Utils {

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
     * Sends an exception to the support server's exception channel.
     * @param ex Exception
     * @param command String
     */
    public static void sendException(Exception ex, String command) {
        try {
            MessageChannel channel = Configuration.BOT.getSelfUser().getJDA().getTextChannelById(495602825355591700L);

            StringBuilder traceString = new StringBuilder();
            for(StackTraceElement trace: ex.getStackTrace()) {
                traceString.append(trace.toString());
                traceString.append("\n");
            }

            channel.sendMessage(command + "\n`" + traceString.toString() + "`").queue();
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
            Cache.BOT_LIST.setStats(Configuration.BOT.getShardInfo().getShardId(), Configuration.BOT.getShardInfo().getShardTotal(), Math.toIntExact(Configuration.BOT.getGuildCache().size()));
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
        System.out.println("          db    db db    db db    db db   dD  .d88b.  ");
        System.out.println("          `8b  d8' 88    88 88    88 88 ,8P' .8P  Y8. ");
        System.out.println("           `8bd8'  88    88 88    88 88,8P   88    88 ");
        System.out.println("             88    88    88 88    88 88`8b   88    88 ");
        System.out.println("             88    88b  d88 88b  d88 88 `88. `8b  d8' ");
        System.out.println("             YP    ~Y8888P' ~Y8888P' YP   YD  `Y88P'  ");
        System.out.println("┏━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[COMMANDS " + Cache.MODULES.size() + "/" + Cache.COMMANDS.size() + "]━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┓");
        System.out.println("┃ " + Cache.LAST_TEN.get(0));
        System.out.println("┃ " + Cache.LAST_TEN.get(1));
        System.out.println("┃ " + Cache.LAST_TEN.get(2));
        System.out.println("┃ " + Cache.LAST_TEN.get(3));
        System.out.println("┃ " + Cache.LAST_TEN.get(4));
        System.out.println("┃ " + Cache.LAST_TEN.get(5));
        System.out.println("┃ " + Cache.LAST_TEN.get(6));
        System.out.println("┃ " + Cache.LAST_TEN.get(7));
        System.out.println("┃ " + Cache.LAST_TEN.get(8));
        System.out.println("┃ " + Cache.LAST_TEN.get(9));
        System.out.println("┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[INFO]━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫");
        System.out.println("┃ " + Cache.LATEST_INFO );
        System.out.println("┣━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━[STATISTICS]━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┫");
        System.out.println("┃ Uptime: " + SystemClock.getRuntime() + ", Ping: " + Cache.PING + ", Guilds: " + Cache.GUILD_COUNT + ", DB Idle: " + Cache.DB_POOL_IDLE + ", DB Active: " + Cache.DB_POOL_ACTIVE);
        System.out.println("┃ Messages processed: " + Cache.MESSAGES_PROCESSED + ", Reacts processed: " + Cache.REACTS_PROCESSED + ", Commands processed: " + Cache.COMMANDS_PROCESSED);
        System.out.println("┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛");
    }

    /**
     * Increments processed integer.
     * @param type int
     */
    public static void incrementEventsProcessed(int type) {
        if(type == 0) {
            Cache.MESSAGES_PROCESSED = (Cache.MESSAGES_PROCESSED + 1);
        } else if(type == 1) {
            Cache.REACTS_PROCESSED = (Cache.REACTS_PROCESSED + 1);
        } else if(type == 2) {
            Cache.COMMANDS_PROCESSED = (Cache.COMMANDS_PROCESSED + 1);
        }
    }

    /**
     * Updates the latest [INFO] message or the latest command message.
     * @param latest String
     */
    public static void updateLatest(String latest) {
        if(latest.startsWith("[INFO]")) {
            Cache.LATEST_INFO = latest;
        } else {
            Cache.LAST_TEN.addFirst(latest);
            if(Cache.LAST_TEN.size() > 10) {
                Cache.LAST_TEN.removeLast();
            }
        }
    }

    /**
     * Gets current songs timeStamp.
     * @param milliseconds; how many milliseconds of the song has played.
     * @return formatted timeStamp.
     */
    public static String getTimestamp(long milliseconds) {
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000 * 60)) % 60);
        int hours   = (int) ((milliseconds / (1000 * 60 * 60)) % 24);

        if(hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        } else {
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

}
