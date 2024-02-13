package davzuzu.spring.security.jwt.controller;

import davzuzu.spring.security.jwt.model.ERole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesController {

    @GetMapping("/access-admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAdmin() {
        String.format("","");
        return "Hola, has accedido con rol de ADMIN";
    }

    @GetMapping("/access-user")
    @PreAuthorize("hasRole('USER')")
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
//    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String accessUser() {
        return "Hola, has accedido con rol de USER";
    }

    @GetMapping("/access-invited")
    @PreAuthorize("hasRole('INVITED')")
    public String accessInvited() {
        return "Hola, has accedido con rol de INVITED";
    }
}
