package basketbandit.core;

import net.dv8tion.jda.core.entities.Guild;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.List;

import static basketbandit.core.BasketBandit.bot;

class Monitor {

    private final JLabel[] labels = new JLabel[12];

    Monitor() {
        JFrame frame = new JFrame("BasketBandit");

        try {
            frame.setIconImage(new ImageIcon(ImageIO.read(getClass().getResourceAsStream("/images/basketbandit.jpg"))).getImage());
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setSize(500, 210);
            frame.setLayout(null);

            for(int i = 0; i < 12; i++) {
                labels[i] = new JLabel("");
                frame.getContentPane().add(labels[i]);
                labels[i].setBounds(10, 2 + (18 * i), 500, 25);
            }

            // Set here because they don't need to be updated at runtime.

            frame.setVisible(true);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Updates the count label.
     * @param msg -> message count.
     * @param cmd -> command count.
     */
    void updateCount(int msg, int cmd) {
        labels[2].setText("Messages Processed: " + msg + ", Commands Processed: " + cmd + ".");
    }

    void updateOneSecond(String runtime) {
        labels[0].setText("Runtime: " + runtime);
    }

    void updateTenSeconds() {
        int users = 0;
        List<Guild> guilds = bot.getGuilds();
        for(Guild guild: guilds) {
            users += guild.getMembers().size();
        }

        labels[3].setText("Version: " + Configuration.VERSION + ", Invocation: " + Configuration.PREFIX);
        labels[4].setText("Servers: " + bot.getGuilds().size() + ", Users: " + users);
        labels[5].setText("Modules: " + BasketBandit.moduleList.size() + ", Commands: " + BasketBandit.commandList.size());

        labels[7].setText("Ping: " + bot.getPing());

    }

    //TODO: Not use swing and try to move over to FX if possible.

}
