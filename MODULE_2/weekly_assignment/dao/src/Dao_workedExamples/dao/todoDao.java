package Dao_workedExamples.dao;

import Dao_workedExamples.entity.todo;


public interface todoDao {
    public void save(todo t);
    public todo findbyId(int id);
    public void update(todo t);
    public void deletebyid(int id);
    public void deleteall();
    public void findall();

}
