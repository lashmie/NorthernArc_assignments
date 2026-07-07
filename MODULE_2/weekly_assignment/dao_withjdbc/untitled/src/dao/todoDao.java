package dao;

import entity.todo;

public interface todoDao {
    public void save(todo t);
    public todo findbyId(int id);
    public void update(todo t);
    public void deletebyid(int id);
    public void deleteall();
    public void findall();

}
