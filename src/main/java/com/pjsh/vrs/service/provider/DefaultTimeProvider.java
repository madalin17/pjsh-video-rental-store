package com.pjsh.vrs.service.provider;

import org.springframework.stereotype.Component;

@Component
public class DefaultTimeProvider implements TimeProvider {
    @Override
    public long now() {
        return System.currentTimeMillis();
    }
}
