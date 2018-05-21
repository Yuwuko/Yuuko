// Program: BasketBandit (Discord Bot)
// Programmer: Joshua Mark Hunt
// Version: 02/05/2018 - JDK 10.0.1

package basketbandit.core;

import javax.swing.*;

class UserInterface{

    private final JFrame frame;
    private final JLabel[] labels = new JLabel[12];

    /**
     * Interface constructor
     */
    UserInterface() {
        frame = new JFrame("BasketBandit " + Configuration.VERSION);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(600,290);
        frame.setLayout(null);
        for(int i = 0; i < 12; i++) {
            labels[i] = new JLabel("");
            frame.getContentPane().add(labels[i]);
            labels[i].setBounds(5,2+(20*i),600,25);
        }
        frame.setVisible(true);
    }

    /**
     * Updates the count label.
     * @param msg -> message count.
     * @param cmd -> command count.
     */
    void updateCount(int msg, int cmd) {
        labels[0].setText("Messages Processed: " + msg + ", Commands Processed: " + cmd + ".");
    }

    /**
     * Updates system runtime.
     * @param d; days.
     * @param h; hours.
     * @param m; minutes.
     * @param s; seconds.
     */
    void updateRuntime(String d, String h, String m, String s) {
        frame.setTitle("BasketBandit " + Configuration.VERSION + " (" + d + ":" + h + ":" + m + ":" + s + ")");
    }

    public void setDebug(String input) {
        labels[1].setText(input);
    }
}
