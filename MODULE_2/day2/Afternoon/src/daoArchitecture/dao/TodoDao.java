package daoArchitecture.dao;


import daoArchitecture.entity.Todo;

import java.util.List;

public interface TodoDao {

    void save(Todo todo);

    Todo findById(int id);

    void update(Todo todo);

    void deleteById(int id);

    List<Todo> findAll();

    void deleteAll();
}