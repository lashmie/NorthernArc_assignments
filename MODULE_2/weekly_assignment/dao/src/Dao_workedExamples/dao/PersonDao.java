package Dao_workedExamples.dao;

import Dao_workedExamples.entity.Person;

import java.util.List;

public interface PersonDao {
    void save(Person person);
    List<Person> findAll();
    void update(Person person);
    void deleteById(String name);
    void deleteAll();

}
