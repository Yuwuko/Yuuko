package basketbandit.core;

class TimeKeeper implements Runnable {

    private final UserInterface ui;

    TimeKeeper(UserInterface ui) {
        this.ui = ui;
        new Thread(this,"TimeKeeper").start();
    }

    @Override
    public void run() {
        try {
            int d = 0, h = 0, m = 0, s = 0;
            String ds, hs, ms, ss;

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
