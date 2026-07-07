package dao;

import entity.todo;

import java.util.Map;

public interface DaoTodo {
    public void save(todo t);
    public todo findbyId(int id);
    public void update(todo t);
    public void deletebyid(int id);
    public void deleteall();
    public Map<Integer,todo> findall();
}
