package com.xism4.sternalboard.api.repository.impl;

import com.xism4.sternalboard.api.SternalBoard;
import com.xism4.sternalboard.api.repository.ObjectCacheRepository;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public class ObjectCacheRepositoryImpl implements ObjectCacheRepository<SternalBoard> {

    private final Map<UUID, SternalBoard> cache = new ConcurrentHashMap<>();

    @Override
    public void create(SternalBoard object) {
        this.cache.put(object.getPlayer().getUniqueId(), object);
    }

    @Override
    public void delete(UUID id) {
        this.cache.remove(id);
    }

    @Override
    public SternalBoard find(UUID id) {
        return this.cache.get(id);
    }

    @Override
    public boolean exists(UUID id) {
        return this.cache.containsKey(id);
    }

    @Override
    public boolean exists(SternalBoard object) {
        return this.cache.containsValue(object);
    }

    @Override
    public void clear() {
        this.cache.clear();
    }

    @Override
    public @NotNull Iterator<SternalBoard> iterator() {
        return this.cache.values().iterator();
    }

    @Override
    public void forEach(Consumer<? super SternalBoard> action) {
        this.cache.values().forEach(action);
    }
}
