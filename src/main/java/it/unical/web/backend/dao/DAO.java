package it.unical.web.backend.dao;

import java.util.List;

public interface DAO<T>{

    List<T> getAll();
    T getById(int id);
    void add(T entity);
    void update(T entity);
    void delete(int id);
}
