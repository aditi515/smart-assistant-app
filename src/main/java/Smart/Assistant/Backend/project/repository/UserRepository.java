package Smart.Assistant.Backend.project.repository;

import Smart.Assistant.Backend.project.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    @Query(value = "SELECT * FROM app_user WHERE email = :email", nativeQuery = true)
    Optional<AppUser> getUserByEmail(@Param("email")String email);

    @Query(value = "SELECT * FROM app_user", nativeQuery = true)
    List<AppUser> findAllUsers();
}
