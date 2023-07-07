package org.thanhmagics.afkshards;

import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ActionBarChecker implements Runnable {

    public static void start() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.submit(new ActionBarChecker());
        executorService.shutdown();
    }

    @Override
    public void run() {
        int phase = ( Integer.parseInt(Objects.requireNonNull(AFKShards.getInstance().getConfig().getString("ActionBar.phase"))));
        int p = Integer.parseInt(Objects.requireNonNull(AFKShards.getInstance().getConfig().getString("AFKReward_Phase"))) / (phase + 1);
        new Timer() {
            String hex1 = Utils.hex(AFKShards.getInstance().getConfig().getString("ActionBar.player.ht"));
            String hex2 = Utils.hex(AFKShards.getInstance().getConfig().getString("ActionBar.player.dt"));
            String cd = AFKShards.getInstance().getConfig().getString("ActionBar.cooldown");

            String str = AFKShards.getInstance().getConfig().getString("ActionBar.format");
            int tick = 0;
            long time_end = System.currentTimeMillis() + ((long) p * (phase + 2));
            @Override
            public void run() {
                if (!AFKShards.status)
                    cancel();
                StringBuilder actionBar = new StringBuilder();
                if (tick > 0)
                    actionBar.append(hex1);
                for (int i = 0; i < tick; i++) {
                    actionBar.append(cd);
                }
                if (tick < phase)
                    actionBar.append(hex2);
                for (int i = tick; i < phase; i++) {
                    actionBar.append(cd);
                }
                long time = (time_end - System.currentTimeMillis());
                double t = (double) time / 1000;
                for (Player player : AFKShards.getInstance().getInAfk().keySet()) {
                    player.sendActionBar(Utils.applyColor(str.replace("{cooldown}",actionBar.toString()).replace("{time}",String.valueOf(withMathRound(t,1)))));
                }
                tick++;
                if (tick > phase) {
                    tick = 0;
                    time_end = System.currentTimeMillis() + ((long) p * (phase + 2));
                }
            }
        }.runTask(p);
    }

    double withMathRound(double value, int places) {
        double scale = Math.pow(10, places);
        return Math.round(value * scale) / scale;
    }
}
