package com.yuuko.core;

import com.yuuko.core.database.DatabaseConnection;
import com.yuuko.core.database.DatabaseFunctions;
import com.yuuko.core.utils.MessageHandler;
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
            MessageHandler.sendException(ex, "public void run() [SystemClock]");
        }
    }

    private void update() {
        runtimeString = ((d < 10) ? String.format("%02d", d) : d + "") + ":" + ((h < 10) ? String.format("%02d", h) : h + "") + ":" + ((m < 10) ? String.format("%02d", m) : m + "") + ":" + ((s < 10) ? String.format("%02d", s) : s + "");

        // 5 seconds
        if(++fs == 5) {
            DatabaseConnection.queryConnections();
            Utils.consoleOutput();
            new DatabaseFunctions().updateServerStatus();
            fs = 0;
        }

        // 5 minutes
        if(++fm == 300) {
            Cache.updatePing();
            fm = 0;
        }

        // 1 hour
        if(++s == 60) {
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
