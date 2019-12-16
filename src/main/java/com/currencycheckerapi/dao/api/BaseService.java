package com.currencycheckerapi.dao.api;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

/**
 * Created by Alexe on 08.04.2017.
 */
public abstract class BaseService<E extends AbstractEntity, R extends BaseRepository<E>> {

    @Autowired
    protected R repository;

    public long count() {
        return repository.count();
    }

    public E save(E entity) {
        return repository.save(entity);
    }

    public Iterable<E> save(Iterable<E> entities) {
        return repository.saveAll(entities);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    public void delete(E entity) {
        repository.delete(entity);
    }

    public void delete(Iterable<E> entities) {
        repository.deleteAll(entities);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public Iterable<E> findAll() {
        return repository.findAll();
    }

    public Iterable<E> findAll(Iterable<String> ids) {
        return repository.findAllById(ids);
    }

    public Optional<E> findOne(String id) {
        return repository.findById(id);
    }

    public E find(String id) {
        return repository.findById(id).orElseThrow();
    }

    public boolean exists(String id) {
        return repository.existsById(id);
    }
}
