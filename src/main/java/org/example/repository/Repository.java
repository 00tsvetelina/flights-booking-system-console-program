package org.example.repository;

import org.example.model.CommonIdClass;

import java.util.List;

public interface Repository<T extends CommonIdClass> {

    List<T> getAll();

    T getById(Integer id);

    T create(T obj);

    T update (T obj);

    void delete(Integer id);

}