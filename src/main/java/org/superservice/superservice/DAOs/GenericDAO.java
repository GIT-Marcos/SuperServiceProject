package org.superservice.superservice.DAOs;

import java.util.List;

/**
 * Operaciones básicas que usan DAOs simples que no requieren complejidad en las consultas a bd y son repetitivas.
 * @param <T> tipo de entidad.
 */
public interface GenericDAO<T, ID> {

    List<T> getAll();

    T getById(ID id);

    void save(T t);

    void update(T t);

    void delete(T t);

}
