package Dao_workedExamples.dao;

import Dao_workedExamples.entity.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonImpl implements PersonDao {
    List<Person> list = new ArrayList();
    @Override
    public void save(Person person) {
        list.add(person);
    }

    @Override
    public List<Person> findAll() {
        return list;
    }

    @Override
    public void update(Person person) {
    for(Person p:list){
        if(p.getName().equalsIgnoreCase(person.getName())){
            p.setAge(person.getAge());
            break;
        }
    }
    }

    @Override
    public void deleteById(String name) {
    for(int i=0;i<list.size();i++){
        if(list.get(i).getName().equalsIgnoreCase(name)){
            list.remove(i);
        }
        break;
    }
    }

    @Override
    public void deleteAll() {
list.clear();
    }
}
