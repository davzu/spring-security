package davzuzu.spring.security.basic.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

// Anotaciones con que indicamos a Spring que vamos a configurar la seguridad de la aplicación
@Configuration
@EnableWebSecurity
public class Securityconfig {

    // SecurityFilterChain: Objeto para configurar el comportamiento de Spring Security
    // httpSecurity: Objeto de Spring Security que nos ayuda a configurar la seguridad
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                // Cross site request forgery por defecto está habilitado
                // Se recomienda tenerlo activo (por defecto) cuando se trabaja con formularios
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/v1/index2").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> form
                        .permitAll()
                        // URL a donde se va a dirigir despues de iniciar sesion
                        .successHandler(successHandler()))
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                        .invalidSessionUrl("/login")
                        // Configuracion para el robo del ID de sesion
                        // migrateSession: Se crea un nuevo ID de sesion y se migra la informacion de la sesion
                        // newSession: Se crea un nuevo ID de sesion pero no migra la informacion de la sesion
                        .sessionFixation(sessionFixation -> sessionFixation.migrateSession())
                        .maximumSessions(1)
                        .expiredUrl("/login")
                        // Ver datos de la sesion del usuario
                        .sessionRegistry(sessionRegistry()))
                // Habilita autenticacion basica donde se envia el Username y Password en el Header del request
                .httpBasic(Customizer.withDefaults())
                .build();

    }

    // Objeto que se va a encargar de administrar todos los registros que se encuentran en las sesiones
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler() {
        return ((request, response, authentication) -> response.sendRedirect("/v1/session"));
    }

}
