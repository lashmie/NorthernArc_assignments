package org.northernarc.jpauserexplanation.repo;

import org.northernarc.jpauserexplanation.model.JpaUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaUserRepo extends JpaRepository<JpaUser, Long> {
  JpaUser findByUsername(String username);
}
