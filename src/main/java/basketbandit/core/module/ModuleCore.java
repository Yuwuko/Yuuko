// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core.module;

import basketbandit.core.Configuration;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.sql.ResultSet;

class ModuleCore {

    private Database database = new Database();
    private MessageReceivedEvent e;
    private String serverLong;

    ModuleCore(MessageReceivedEvent e) {
        this.e = e;
        MessageChannel channel = e.getChannel();
        User user = e.getAuthor();
        serverLong = e.getGuild().getIdLong() + "";
        String[] command = e.getMessage().getContentRaw().split("\\s+", 2);

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

        if(command[0].toLowerCase().equals(Configuration.PREFIX + "module") || e.getMember().hasPermission(Permission.ADMINISTRATOR) && command[0].toLowerCase().equals(Configuration.PREFIX + "module")) {
            if(command[1].toLowerCase().equals("dev")) {
                if(user.getIdLong() == 215161101460045834L) {
                    commandToggle(command);
                }
            } else {
                commandToggle(command);
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

            for(int i = 4; i < 9; i++) {
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
                    .setDescription("Each module can be toggled on or off by using the " + Configuration.PREFIX + "module <module name> command.")
                    .addField("Enabled Modules", enabled.toString().replace(",","\n").replace("MOD", "").replace("[", "").replace("]", "").replace(" ", "").toLowerCase(), false)
                    .addField("Disabled Modules", disabled.toString().replace(",","\n").replace("MOD", "").replace("[", "").replace("]", "").replace(" ", "").toLowerCase(), false)
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
                .setAuthor("Hey " + e.getAuthor().getName() + ",",null,e.getAuthor().getAvatarUrl())
                .setTitle("Below are a list of available commands!")
                .setDescription(
                        Configuration.PREFIX + "hello                              ::  Simply greets you. \n" +
                                Configuration.PREFIX + "roll <value>                 ::  Rolls for a given number, within the bounds of computation. \n" +
                                Configuration.PREFIX + "nuke <value>              ::  Removes self and then a set amount of messages. (ADMIN) \n" +
                                Configuration.PREFIX + "overreact                     ::  Overreacts to the previous message. \n" +
                                Configuration.PREFIX + "insult                            \u200a::  Insults a random person on the server. \n" +
                                Configuration.PREFIX + "rsstats <value>          \u200a::  Retrieves RuneScape 3 stats for the given player. \n" +
                                Configuration.PREFIX + "osstats <value>         \u200a\u200a::  Retrieves Oldschool RuneScape stats for the given player. \n" +
                                Configuration.PREFIX + "info <value>                ::  Retrieves information about a user. \n" +
                                Configuration.PREFIX + "sum <value>               ::  Performs simple 2 variable sums. Operators: (+, -, *, /, ^, %) \n"

                )
                .addField("Features", "I am able to log commands sent by adding a text-channel named \"command-log\".", false)
                .addField("Want me on your server?", "Click [here](https://discordapp.com/oauth2/authorize?client_id=420682957007880223&permissions=8&scope=bot) to send me an invite! Also be sure to give me admin privileges if you wish to use the " + Configuration.PREFIX + "nuke command or any other admin commands.", false)
                .setFooter("Version: " + Configuration.VERSION, e.getGuild().getMemberById(Configuration.BOT_ID).getUser().getAvatarUrl())
                ;
        e.getTextChannel().sendMessage("Check your private messages, " + e.getAuthor().getAsMention() + "! <:ShinobuOshino:420423622663077889>").queue();
        e.getAuthor().openPrivateChannel().queue((privateChannel) -> privateChannel.sendMessage(commandInfo.build()).queue());
    }

    /**
     * Toggles a module.
     */
    private void commandToggle(String[] command) {
        if(database.toggleModule("mod" + command[1].toLowerCase(), serverLong)) {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.GREEN).setAuthor(command[1] + " was enabled on this server!");
            e.getTextChannel().sendMessage(embed.build()).queue();
        } else {
            EmbedBuilder embed = new EmbedBuilder().setColor(Color.RED).setAuthor(command[1] + " was disabled on this server!");
            e.getTextChannel().sendMessage(embed.build()).queue();
        }
    }

}
