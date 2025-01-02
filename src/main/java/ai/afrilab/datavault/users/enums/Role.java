package ai.afrilab.datavault.users.enums;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ai.afrilab.datavault.users.enums.Permission.*;

public enum Role {

  SUPER_ADMIN(
      Set.of(
          SUPER_ADMIN_CREATE,
          SUPER_ADMIN_READ,
          SUPER_ADMIN_UPDATE,
          SUPER_ADMIN_DELETE,

          ADMIN_CREATE,
          ADMIN_READ,
          ADMIN_UPDATE,
          ADMIN_DELETE,

          UPLOADER_CREATE,
          UPLOADER_READ,
          UPLOADER_UPDATE,
          UPLOADER_DELETE,

          WORKER_CREATE,
          WORKER_READ,
          WORKER_UPDATE,
          WORKER_DELETE

      )
  ),
  ADMIN(
      Set.of(
          ADMIN_CREATE,
          ADMIN_READ,
          ADMIN_UPDATE,
          ADMIN_DELETE,

          UPLOADER_CREATE,
          UPLOADER_READ,
          UPLOADER_UPDATE,
          UPLOADER_DELETE,

          WORKER_CREATE,
          WORKER_READ,
          WORKER_UPDATE,
          WORKER_DELETE
      )
  ),
  UPLOADER(
      Set.of(
          UPLOADER_CREATE,
          UPLOADER_READ,
          UPLOADER_UPDATE,
          UPLOADER_DELETE
      )
  ),
  WORKER(
      Set.of(
          WORKER_CREATE,
          WORKER_READ,
          WORKER_UPDATE,
          WORKER_DELETE
      )
  );

  private final Set<Permission> permissions;

  private Role(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  public List<SimpleGrantedAuthority> getAuthorities() {
    List<SimpleGrantedAuthority> authorities = new ArrayList<>(getPermissions()
        .stream().map(permission -> new SimpleGrantedAuthority(permission.getPermission())).toList());
    authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
    return authorities;
  }

  public Set<Permission> getPermissions() {
    return this.permissions;
  }
}
