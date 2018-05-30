package basketbandit.core;

public class TimeKeeper implements Runnable {

    public static String runtime;
    private static long ping;

    TimeKeeper() {
        new Thread(this,"TimeKeeper").start();
    }

    @Override
    public void run() {
        try {
            // Wait 5 seconds to give the system time to boot.
            Thread.sleep(5000);

            int d = 0, h = 0, m = 0, s = 0;
            String ds, hs, ms, ss;
            int fm = 0, oh = 0;


            while(true) {
                Thread.sleep(1000);

                fm++;
                if(fm == 300) {
                    fm = 0;
                }

                oh++;
                if(oh == 3600) {
                    oh = 0;
                    ping = BasketBandit.bot.getPing();
                }

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

                runtime = ds + ":" + hs + ":" + ms + ":" + ss;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
