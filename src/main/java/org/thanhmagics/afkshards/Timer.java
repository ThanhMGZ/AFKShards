package org.thanhmagics.afkshards;

public abstract class Timer implements Runnable {

    private long t1 = System.currentTimeMillis();

    private boolean cancel = false;

    public void runTask(int p) {
        while (!cancel) {
            if (t1 < System.currentTimeMillis()) {
                t1 = System.currentTimeMillis() + p;
                run();
            }
        }
    }

    public void cancel() {
        cancel = true;
    }

}
