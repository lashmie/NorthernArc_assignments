package org.northernarc.applicationspringjdbc.repository;

import org.northernarc.applicationspringjdbc.model.Capex;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CapexDao extends JpaRepository<Capex, Long> {
}

