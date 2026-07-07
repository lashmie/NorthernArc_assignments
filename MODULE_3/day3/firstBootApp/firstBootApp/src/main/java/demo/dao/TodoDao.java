package demo.dao;

import demo.model.Todo;

import java.util.List;

public interface TodoDao {
    public void save(Todo todo);
    public Todo findById(int id);
    public List<Todo> findAll();
    public void deleteById(int id);
    public void deleteAll();

}
