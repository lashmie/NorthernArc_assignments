package org.northernarc.applicationspringjdbc.repository;

import org.northernarc.applicationspringjdbc.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonDao extends JpaRepository<Person, Long> {
}
