package org.shannon.spring_rest_play.repositories;

import lombok.Synchronized;
import org.shannon.spring_rest_play.Account;
import org.springframework.data.repository.CrudRepository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class InMemoryRepository<T, ID extends Serializable> implements CrudRepository<T, ID> {
    private Map<ID, T> entities = new HashMap<>();

    protected abstract T withGeneratedValues(T entity);
    protected abstract void additionalLookupsAdd(T entity);
    protected abstract void additionalLookupsRemove(T entity);
    protected abstract void additionalLookupsClear();
    protected abstract ID getId(T entity);

    @Override
    @Synchronized
    public T save(T entity) {
        entity = withGeneratedValues(entity);
        entities.put(getId(entity), entity);
        additionalLookupsAdd(entity);
        return entity;
    }

    @Override
    @Synchronized
    public Iterable<T> save(Iterable<? extends T> entities) {
        entities.forEach(this::save);
        return (Iterable<T>)entities;
    }

    @Override
    @Synchronized
    public T findOne(ID id) {
        return entities.get(id);
    }

    @Override
    @Synchronized
    public boolean exists(ID id) {
        return entities.containsKey(id);
    }

    @Override
    @Synchronized
    public Iterable<T> findAll() {
        return entities.values();
    }

    @Override
    @Synchronized
    public long count() {
        return entities.size();
    }

    @Override
    @Synchronized
    public void delete(ID id) {
        delete(entities.get(id));
    }

    @Override
    @Synchronized
    public void delete(T entity) {
        entities.remove(getId(entity));
        additionalLookupsRemove(entity);
    }

    @Override
    @Synchronized
    public void delete(Iterable<? extends T> entities) {
        entities.forEach(this::delete);
    }

    @Override
    @Synchronized
    public void deleteAll() {
        entities.clear();
        additionalLookupsClear();
    }
}
