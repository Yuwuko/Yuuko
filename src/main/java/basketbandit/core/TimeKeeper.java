package basketbandit.core;

public class TimeKeeper implements Runnable {

    private UserInterface ui;

    public TimeKeeper(UserInterface ui) {
        this.ui = ui;
        new Thread(this,"TimeKeeper").start();
    }

    @Override
    public void run() {
        try {
            int d = 0;
            int h = 0;
            int m = 0;
            int s = 0;

            String ds = "00";
            String hs = "00";
            String ms = "00";
            String ss = "00";

            while(true) {
                Thread.sleep(1000);

                s++;

                if(s == 60) {
                    m++;
                    s = 0;
                    if(m == 60) {
                        h++;
                        m = 0;
                        if(h == 24) {
                            d++;
                            h = 0;
                        }
                    }
                }

                // Add leading zeros.
                ds = (d < 10) ? String.format("%02d", d) : d + "";
                hs = (h < 10) ? String.format("%02d", h) : h + "";
                ms = (m < 10) ? String.format("%02d", m) : m + "";
                ss = (s < 10) ? String.format("%02d", s) : s + "";

                ui.updateRuntime(ds,hs,ms,ss);



            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
