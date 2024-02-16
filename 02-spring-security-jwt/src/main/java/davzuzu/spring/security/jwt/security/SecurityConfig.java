package davzuzu.spring.security.jwt.security;

import davzuzu.spring.security.jwt.model.ERole;
import davzuzu.spring.security.jwt.security.filter.JwtAuthenticationFilter;
import davzuzu.spring.security.jwt.security.filter.JwtAuthorizationFilter;
import davzuzu.spring.security.jwt.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
// @EnableGlobalMethodSecurity: Habilita anotaciones de Spring Security para los controladores
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                            AuthenticationManager authenticationManager) throws Exception {

        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtUtils);
        jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);

        // Ruta poder autenticarnos. Por default es /login
        // jwtAuthenticationFilter.setFilterProcessesUrl("");

        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/hello").permitAll()
//                        .requestMatchers("/access-admin").hasRole(ERole.ADMIN.name())
//                        .requestMatchers("/access-user").hasRole(ERole.USER.name())
//                        .requestMatchers("/access-invited").hasRole(ERole.INVITED.name())
                        // Trabajar un request con varios roles
//                        .requestMatchers("/access-user").hasAnyRole(ERole.ADMIN.name(), ERole.USER.name())
                        .anyRequest().authenticated()
                        )
                .sessionManagement(sessionManagement -> sessionManagement
                        // STATELESS: No crea ninguna sesion, no trabaja con sesiones. Todas las solicitudes
                        // las trabajara de forma independiente (Para este caso se evalua el token enviado)
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // Primero se verificara el token con jwtAuthorizationFilter y si no encuentra
                // el token validara por usuario y clave en jwtAuthenticationFilter
                .addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilter(jwtAuthenticationFilter)
                //.httpBasic(Customizer.withDefaults()) Se comenta porque se trabajara con JWT
                .build();
    }

    // Objeto encriptador de claves que sera usado en el authenticationManager
    @Bean
    PasswordEncoder passwordEncoder() {
        // Encripta en una sola via. Una vez que se encripta, no se puede desencriptar
        // Siempre genera encriptaciones diferentes, por lo que hace aun mas complejo la desencriptacion
        return new BCryptPasswordEncoder();
    }

    // Objeto que se encargara de la administracion de la autenticacion de los usuario
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Tomara los objetos userDetailsService y passwordEncoder
        return authenticationConfiguration.getAuthenticationManager();
    }

    // ================== Pruebas con autenticacion basica ==================

//    @Bean
//    PasswordEncoder passwordEncoder() {
//        // Sin encriptar el password
//        return NoOpPasswordEncoder.getInstance();
//    }

//    @Bean
//    UserDetailsService userDetailsService() {
//        // Se creara un usuario en memoria provisionalmente para autenticarnos
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(User.withUsername("david").password("1234").roles().build());
//
//        return manager;
//    }

}
