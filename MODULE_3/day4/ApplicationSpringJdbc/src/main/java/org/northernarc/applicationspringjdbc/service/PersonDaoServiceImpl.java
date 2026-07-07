package org.northernarc.applicationspringjdbc.service;

import org.northernarc.applicationspringjdbc.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonDaoServiceImpl implements PersonDaoService{
    @Autowired
    Person persons;
    @Override
    public Person save(Person person) {
        persons.
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Person update(int id, Person person) {
        return null;
    }

    @Override
    public Person findByid(int id) {
        return null;
    }
}
