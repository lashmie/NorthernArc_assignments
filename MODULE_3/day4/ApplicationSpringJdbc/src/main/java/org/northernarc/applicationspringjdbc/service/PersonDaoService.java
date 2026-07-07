package org.northernarc.applicationspringjdbc.service;

import org.northernarc.applicationspringjdbc.model.Person;

public interface PersonDaoService {
    Person save(Person person);
    void delete(int id);
    Person update(int id,Person person);
    Person findByid(int id);
}
