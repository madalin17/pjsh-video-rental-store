package com.pjsh.vrs.controller.future;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

@Component
public class RecommendationFutureStore {

    private Map<Long, Future<?>> futureMap;

    public RecommendationFutureStore() {
        this.futureMap = new ConcurrentHashMap<>();
    }

    public Future<?> get(Long customerId) {
        return futureMap.get(customerId);
    }

    public Future<?> getOrDefault(Long customerId, Future<?> defaultFuture) {
        return this.containsKey(customerId) ? this.get(customerId) : defaultFuture;
    }

    public void put(Long customerId, Future<?> future) {
        futureMap.put(customerId, future);
    }

    public void remove(Long customerId) {
        futureMap.remove(customerId);
    }

    public boolean containsKey(Long customerId) {
        return futureMap.containsKey(customerId);
    }
}

