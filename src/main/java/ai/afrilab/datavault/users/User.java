package ai.afrilab.datavault.users;

import ai.afrilab.datavault.users.enums.Role;
import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

@Entity
@Table(name = "_users")
@EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  @Column(nullable = false)
  private UUID id;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "username", unique = true)
  private String username;

  @Column(name = "email", unique = true)
  private String email;

  @Column(name = "phone")
  private String phone;

  @Column(name = "password")
  private String password;

  @Enumerated(EnumType.STRING)
  @Column(name = "role", nullable = false)
  private Role role;

  @ManyToOne
  @JoinColumn(name = "created_by", nullable = false, updatable = false)
  @CreatedBy
  private User createdBy;

  @CreatedDate
  @Column(name = "created_date", nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @Column(name = "enabled", nullable = false)
  private boolean enabled;

  @Column(name = "locked", nullable = false)
  private boolean locked;

  public User(UUID id, String fullName, String username, String email, String phone, String password, Role role, User createdBy, LocalDateTime createdDate, boolean enabled, boolean locked) {
    this.id = id;
    this.fullName = fullName;
    this.username = username;
    this.email = email;
    this.phone = phone;
    this.password = password;
    this.role = role;
    this.createdBy = createdBy;
    this.createdDate = createdDate;
    this.enabled = enabled;
    this.locked = locked;
  }

  public User() {
  }

  public static UserBuilder builder() {
    return new UserBuilder();
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return role.getAuthorities();
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return !locked;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return enabled;
  }

  @Override
  public String getName() {
    return username;
  }

  public UUID getId() {
    return this.id;
  }

  public String getFullName() {
    return this.fullName;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPhone() {
    return this.phone;
  }

  public Role getRole() {
    return this.role;
  }

  public User getCreatedBy() {
    return this.createdBy;
  }

  public LocalDateTime getCreatedDate() {
    return this.createdDate;
  }

  public boolean isLocked() {
    return this.locked;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public void setCreatedBy(User createdBy) {
    this.createdBy = createdBy;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public static class UserBuilder {
    private UUID id;
    private String fullName;
    private String username;
    private String email;
    private String phone;
    private String password;
    private Role role;
    private User createdBy;
    private LocalDateTime createdDate;
    private boolean enabled;
    private boolean locked;

    UserBuilder() {
    }

    public UserBuilder id(UUID id) {
      this.id = id;
      return this;
    }

    public UserBuilder fullName(String fullName) {
      this.fullName = fullName;
      return this;
    }

    public UserBuilder username(String username) {
      this.username = username;
      return this;
    }

    public UserBuilder email(String email) {
      this.email = email;
      return this;
    }

    public UserBuilder phone(String phone) {
      this.phone = phone;
      return this;
    }

    public UserBuilder password(String password) {
      this.password = password;
      return this;
    }

    public UserBuilder role(Role role) {
      this.role = role;
      return this;
    }

    public UserBuilder createdBy(User createdBy) {
      this.createdBy = createdBy;
      return this;
    }

    public UserBuilder createdDate(LocalDateTime createdDate) {
      this.createdDate = createdDate;
      return this;
    }

    public UserBuilder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public UserBuilder locked(boolean locked) {
      this.locked = locked;
      return this;
    }

    public User build() {
      return new User(this.id, this.fullName, this.username, this.email, this.phone, this.password, this.role, this.createdBy, this.createdDate, this.enabled, this.locked);
    }

    public String toString() {
      return "User.UserBuilder(id=" + this.id + ", fullName=" + this.fullName + ", username=" + this.username + ", email=" + this.email + ", phone=" + this.phone + ", password=" + this.password + ", role=" + this.role + ", createdBy=" + this.createdBy + ", createdDate=" + this.createdDate + ", enabled=" + this.enabled + ", locked=" + this.locked + ")";
    }
  }
}
