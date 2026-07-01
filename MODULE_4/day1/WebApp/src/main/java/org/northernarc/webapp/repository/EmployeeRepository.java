package org.northernarc.webapp.repository;

import jakarta.transaction.Transactional;
import org.northernarc.webapp.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Modifying
    @Transactional
    @Query("""
        update Employee e
        set e.email = :email
        where e.name = :name
    """)
    int updateEmployeeEmailByNames(@Param("name") String name, @Param("email") String email);
}
