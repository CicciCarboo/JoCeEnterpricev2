package se.joce.springv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.joce.springv2.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
