// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 15/05/2018 - JDK 10.0.1

package basketbandit.core.modules;

import basketbandit.core.Configuration;
import basketbandit.core.Database;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class ModuleCore {

    private Database database = new Database();
    private MessageReceivedEvent e;
    private String[] command;
    private String serverLong;

    public ModuleCore(MessageReceivedEvent e) {
        this.e = e;
        MessageChannel channel = e.getChannel();
        User user = e.getAuthor();
        serverLong = e.getGuild().getIdLong() + "";
        command = e.getMessage().getContentRaw().split("\\s+", 2);

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "setup")) {
            if(e.getMember().isOwner() || e.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                if(commandSetup()) {
                    channel.sendMessage("Server setup successful. (You cannot do this again!)").queue();
                } else {
                    channel.sendMessage("Server setup was unsuccessful. (Are you sure the setup command has not been run before?)").queue();
                }
            }
            return;
        }

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "modules")) {
            commandModules();
            return;
        }

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "help")) {
            commandHelp();
            return;
        }

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "modules") || e.getMember().hasPermission(Permission.ADMINISTRATOR) && command[0].toLowerCase().equals(Configuration.PREFIX + "modules")) {
            if(command[1].toLowerCase().equals("dev")) {
                if(user.getIdLong() == 215161101460045834L) {
                    commandToggle();
                }
            } else {
                commandToggle();
            }
            return;
        }

        System.out.println("Somehow got to the end of the core constructor...");
    }

    /**
     * Sets up the database for a particular server. (Admins only)
     * @return if the setup was successful.
     */
    private boolean commandSetup() {
        return database.addNewServer(serverLong);
    }

    /**
     * Returns a the list of active and inactive modules.
     */
    private void commandModules() {
        try {
            ArrayList<String> enabled = new ArrayList<>();
            ArrayList<String> disabled = new ArrayList<>();

            ResultSet resultSet = database.getModuleSettings(serverLong);
            resultSet.next();

            for(int i = 4; i < 12; i++) {
                ResultSetMetaData meta = resultSet.getMetaData();
                if(resultSet.getBoolean(i)) {
                    enabled.add(meta.getColumnName(i));
                } else {
                    disabled.add(meta.getColumnName(i));
                }
            }

            EmbedBuilder commandModules = new EmbedBuilder()
                    .setColor(Color.RED)
                    .setAuthor("Hey " + e.getAuthor().getName() + ",",null,e.getAuthor().getAvatarUrl())
                    .setTitle("Below are the list of bot modules!")
                    .setDescription("Each modules can be toggled on or off by using the " + Configuration.PREFIX + "modules <modules name> command.")
                    .addField("Enabled Modules", enabled.toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), false)
                    .addField("Disabled Modules", disabled.toString().replace(",","\n").replaceAll("[\\[\\] ]", "").toLowerCase(), false)
                    .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl());

            e.getTextChannel().sendMessage(commandModules.build()).queue();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Help command private messages the user all available commands.
     */
    private void commandHelp() {
        EmbedBuilder commandInfo = new EmbedBuilder()
                .setColor(Color.RED)
                .setTitle("Hey " + e.getAuthor().getName() + ",")
                .setDescription(
                        "A full list of commands and features is available on my GitHub, which is located [here](https://github.com/Galaxiosaurus/BasketBandit)! \n" +
                        "If you would like to suggest new features or have any general comments you can send them to my creator [here](https://discord.gg/QcwghsA)! \n\n" +

                        "P.S, commands used to be listed here but formatting is a pain and nobody has time for that."
                )
                .addField("Want me on your server?", "Click [here](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) to send me an invite! Also be sure to give me admin privileges if you wish to use the " + Configuration.PREFIX + "nuke command or any other admin commands.", false)
                .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl())
                ;
        e.getTextChannel().sendMessage("Check your private messages, " + e.getAuthor().getAsMention() + "! <:ShinobuOshino:420423622663077889>").queue();
        e.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());
    }

    /**
     * Toggles a modules.
     */
    private void commandToggle() {
        if(database.toggleModule("mod" + command[1].toLowerCase(), serverLong)) {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setAuthor(command[1] + " was enabled on this server!");
            e.getTextChannel().sendMessage(embed.build()).queue();
        } else {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor(command[1] + " was disabled on this server!");
            e.getTextChannel().sendMessage(embed.build()).queue();
        }
    }

}
