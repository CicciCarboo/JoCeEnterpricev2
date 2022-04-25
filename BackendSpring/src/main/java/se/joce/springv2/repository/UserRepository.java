package se.joce.springv2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import se.joce.springv2.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String name);

    @Query(value="SELECT * FROM users WHERE user_role= 'ADMIN'", nativeQuery=true)
    List<User> getAllAdmin();
}
