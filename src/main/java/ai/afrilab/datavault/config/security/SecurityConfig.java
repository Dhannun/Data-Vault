package ai.afrilab.datavault.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static ai.afrilab.datavault.users.enums.Permission.*;
import static ai.afrilab.datavault.users.enums.Role.ADMIN;
import static ai.afrilab.datavault.users.enums.Role.SUPER_ADMIN;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

  private final AuthenticationProvider authenticationProvider;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final LogoutHandler logoutHandler;

  /**
   * These are the list of path that will be allowed to access without authentication, <b>check Line 68, the permitAll()</b> will make any URL form the WHITE_LIST accessible without authentication <br>
   * As the name WHITE_LIST says, can be accessed publicly, the <b>auth</b> is for authentication which it not suppose tobe secured <br>
   * And the remaining are for OpenAPI (Swagger) Documentations
   */
  private static final String[] WHITE_LIST = {
      "/",
      "/api/v1/auth/**",
      "/v2/api-docs",
      "/v3/api-docs",
      "/v3/api-docs/**",
      "/swagger-resources",
      "/swagger-resources/**",
      "/configuration/ui",
      "/configuration/security",
      "/swagger-ui/**",
      "/webjars/**",
      "/swagger-ui.html"
  };

  public SecurityConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter, LogoutHandler logoutHandler) {
    this.authenticationProvider = authenticationProvider;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    this.logoutHandler = logoutHandler;
  }

  /**
   * Both SUPER_ADMIN and ADMIN can access the <b>users</b> endpoints <br>
   * But Only the SUPER_ADMIN can perform all the [ POST, GET, UPDATE and DELETE ] request on the <b>users</b> endpoints <br>
   * The Normal ADMIN can only perform a Read-Only [ GET ] request on the <b>users</b> endpoints <br>
   * Check the Role and Permission Based Authentication section
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    final String ADMINS_SECURED_ROUTE = "/api/v1/admins/**";
    http
        .cors(withDefaults())
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(
            request -> request
                .requestMatchers(
                    WHITE_LIST
                ).permitAll()

                // Role Based Authorization [ Users Endpoints ]
                .requestMatchers(ADMINS_SECURED_ROUTE).hasAnyRole(
                    SUPER_ADMIN.name(),
                    ADMIN.name()
                )

                // FIXME: ADMIN can also do what super admin can.
                // Permission Based Authorization [ Users Endpoints ]
                .requestMatchers(POST, ADMINS_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_CREATE.name(), ADMIN_CREATE.name())
                .requestMatchers(GET, ADMINS_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_READ.name(), ADMIN_READ.name())
                .requestMatchers(PUT, ADMINS_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_UPDATE.name(), ADMIN_UPDATE.name())
                .requestMatchers(DELETE, ADMINS_SECURED_ROUTE).hasAnyAuthority(SUPER_ADMIN_DELETE.name(), ADMIN_DELETE.name())

                .anyRequest()
                .authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
        .authenticationProvider(authenticationProvider)
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class
        )
        .logout(
            logout ->
                logout.addLogoutHandler(logoutHandler)
                    .logoutUrl("/api/v1/auth/logout")
                    .logoutSuccessHandler(
                        (request, response, authentication) -> SecurityContextHolder.clearContext()
                    )
        );

    return http.build();
  }
}
