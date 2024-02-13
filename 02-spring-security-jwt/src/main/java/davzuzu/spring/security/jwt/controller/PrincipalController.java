package davzuzu.spring.security.jwt.controller;

import davzuzu.spring.security.jwt.controller.request.CreateUserDTO;
import davzuzu.spring.security.jwt.model.ERole;
import davzuzu.spring.security.jwt.model.RoleEntity;
import davzuzu.spring.security.jwt.model.UserEntity;
import davzuzu.spring.security.jwt.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class PrincipalController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/hello")
    public String hello() {
        return "hello World Not Secured";
    }

    @GetMapping("/hello-secured")
    public String helloSecured() {
        return "Hello World Secured";
    }

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        Set<RoleEntity> roles = createUserDTO.getRoles()
                                                .stream()
                                                .map(role-> RoleEntity.builder()
                                                                        .name(ERole.valueOf(role))
                                                                        .build())
                                                .collect(Collectors.toSet());

        UserEntity userEntity = UserEntity.builder()
                .username(createUserDTO.getUsername())
                .password(passwordEncoder.encode(createUserDTO.getPassword()))
                .email(createUserDTO.getEmail())
                .roles(roles)
                .build();

        userRepository.save(userEntity);

        return ResponseEntity.ok(userEntity);
    }

    @DeleteMapping("delete-user")
    public String deleteUser(@RequestParam String id) {
        userRepository.deleteById(Long.parseLong(id));

        return "Se ha borrado el user con id ".concat(id);
    }
}
