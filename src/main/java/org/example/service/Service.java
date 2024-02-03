package org.example.service;

import org.example.model.BaseId;

public interface Service<T extends BaseId> {
    String getAll();

    String getById(Integer id);

    String create(T obj);

    String update(T obj);

    String deleteById(Integer id);

}
