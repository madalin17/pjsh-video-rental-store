package com.pjsh.vrs.service.provider;

public class TestTimeProvider implements TimeProvider {

    private long currentTime;

    public TestTimeProvider(long initialTime) {
        this.currentTime = initialTime;
    }

    @Override
    public long now() {
        return currentTime;
    }

    public void advanceTime(long milliseconds) {
        currentTime += milliseconds;
    }
}