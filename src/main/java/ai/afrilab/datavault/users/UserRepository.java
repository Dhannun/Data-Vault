package ai.afrilab.datavault.users;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  @Query("select u from User u where upper(u.username) = upper(?1)")
  Optional<User> findByUsernameIgnoreCase(String username);

  @Query("select u from User u where u.role = 'SUPER_ADMIN'")
  Optional<User> getSuperAdmin();

  boolean existsByUsernameOrEmailAllIgnoreCase(String username, String email);

  @Query("select u from User u order by u.createdDate desc")
  Page<User> findAllOrderByCreatedDate(Pageable pageable);
}