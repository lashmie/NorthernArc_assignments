package org.northernarc.applicationspringjdbc.repository;


import org.northernarc.applicationspringjdbc.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookDao extends JpaRepository<Book,Integer> {

}
