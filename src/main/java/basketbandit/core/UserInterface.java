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
     * @param runtime -> bot runtime
     */
    void updateRuntime(String runtime) {
        frame.setTitle("BasketBandit " + Configuration.VERSION + " " + runtime);
    }

}
