package com.basketbandit.core;

import com.basketbandit.core.utils.Utils;

public class SystemClock implements Runnable {

    private static boolean running;
    private static String runtime;

    SystemClock() {
        running = true;
        new Thread(this,"SystemClock").start();
    }

    @Override
    public void run() {
        try {
            // Wait 5 seconds to give the system time to boot.
            Thread.sleep(10000);

            int d = 0, h = 0, m = 0, s = 0;
            String ds, hs, ms, ss;
            int fm = 0, oh = 0, fs =0;

            SystemInformation.updatePing();

            while(running) {
                Thread.sleep(1000);

                fs++;
                if(fs == 5) {
                    Utils.consoleOutput();
                    fs = 0;
                }

                fm++;
                if(fm == 300) {
                    SystemInformation.updatePing();
                    fm = 0;
                }

                oh++;
                if(oh == 3600) {
                    oh = 0;
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
        } catch (InterruptedException ex) {
            Utils.sendException(ex, "public void run() [SystemClock]");
        }
    }

    public static String getRuntime() {
        return runtime;
    }

    public static void toggleClock() {
        running = !running;
    }
}
