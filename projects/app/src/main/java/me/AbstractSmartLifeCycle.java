package me;

import org.springframework.context.SmartLifecycle;

public abstract class AbstractSmartLifeCycle implements SmartLifecycle {

    private boolean isRunning;

    protected void onStart() {
    }

    protected void onStop() {
    }

    @Override
    public void stop() {
        onStop();
        isRunning = false;
    }

    @Override
    public void start() {
        onStart();
        isRunning = true;
    }

    @Override
    public boolean isAutoStartup() {
        return true;
    }

    @Override
    public void stop(Runnable callback) {
        try {
            stop();
        } finally {
            callback.run();
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public int getPhase() {
        return 0;
    }
}
