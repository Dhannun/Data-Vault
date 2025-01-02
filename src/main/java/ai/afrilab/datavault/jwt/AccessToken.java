package ai.afrilab.datavault.jwt;


import ai.afrilab.datavault.users.User;
import jakarta.persistence.*;

import java.util.UUID;


@Entity
@Table(name = "access_token")
public class AccessToken {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false)
    private UUID id;

    @Column(name = "token")
    private String token;

    @Column(name = "revoked")
    private boolean revoked;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public AccessToken(UUID id, String token, boolean revoked, User user) {
        this.id = id;
        this.token = token;
        this.revoked = revoked;
        this.user = user;
    }

    public AccessToken() {
    }

    public static AccessTokenBuilder builder() {
        return new AccessTokenBuilder();
    }

    public UUID getId() {
        return this.id;
    }

    public String getToken() {
        return this.token;
    }

    public boolean isRevoked() {
        return this.revoked;
    }

    public User getUser() {
        return this.user;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static class AccessTokenBuilder {
        private UUID id;
        private String token;
        private boolean revoked;
        private User user;

        AccessTokenBuilder() {
        }

        public AccessTokenBuilder id(UUID id) {
            this.id = id;
            return this;
        }

        public AccessTokenBuilder token(String token) {
            this.token = token;
            return this;
        }

        public AccessTokenBuilder revoked(boolean revoked) {
            this.revoked = revoked;
            return this;
        }

        public AccessTokenBuilder user(User user) {
            this.user = user;
            return this;
        }

        public AccessToken build() {
            return new AccessToken(this.id, this.token, this.revoked, this.user);
        }

        public String toString() {
            return "AccessToken.AccessTokenBuilder(id=" + this.id + ", token=" + this.token + ", revoked=" + this.revoked + ", user=" + this.user + ")";
        }
    }
}
