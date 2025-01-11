package com.pjsh.vrs.service.provider;

public interface TimeProvider {
    long now();

    void advanceTime(long milliseconds);
}
