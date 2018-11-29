package com.yuuko.core;

import com.yuuko.core.database.DatabaseConnection;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utils.Utils;

public class SystemClock implements Runnable {

    private static boolean running;
    private static int runtime;
    private static String runtimeString;
    private static int d = 0, h = 0, m = 0, s = 0;

    private static int fm = 0, oh = 0, fs =0;

    SystemClock() {
        runtime = 0;
        running = true;

        new Thread(this,"SystemClock").start();
    }

    @Override
    public void run() {
        try {
            // Wait 5 seconds to give the system time to boot.
            Thread.sleep(10000);

            Cache.updatePing();

            while(running) {
                Thread.sleep(1000);
                runtime++;
                update();
            }
        } catch (InterruptedException ex) {
            Utils.sendException(ex, "public void run() [SystemClock]");
        }
    }

    private void update() {
        fs++;
        // Every 5 seconds
        if(fs == 5) {
            DatabaseConnection.queryConnections();
            Utils.consoleOutput();
            new DatabaseFunctions().updateServerStatus();
            fs = 0;
        }

        fm++;
        // Every 5 minutes
        if(fm == 300) {
            Cache.updatePing();
            fm = 0;
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
        String ds = (d < 10) ? String.format("%02d", d) : d + "";
        String hs = (h < 10) ? String.format("%02d", h) : h + "";
        String ms = (m < 10) ? String.format("%02d", m) : m + "";
        String ss = (s < 10) ? String.format("%02d", s) : s + "";

        runtimeString = ds + ":" + hs + ":" + ms + ":" + ss;
    }

    public static String getRuntimeString() {
        return runtimeString;
    }

    public static int getRuntime() {
        return runtime;
    }

    public static void toggleClock() {
        running = !running;
    }
}
