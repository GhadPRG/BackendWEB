package it.unical.web.backend.persistence.dao.DAOInterface;

import java.util.List;

public interface BaseDAO<T> {
    T findById(int id);
    List<T> findAll(long id);
    boolean save(T entity);
    boolean update(T entity);
    boolean delete(int id);
}