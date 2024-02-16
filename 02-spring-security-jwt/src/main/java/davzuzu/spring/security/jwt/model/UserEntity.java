package davzuzu.spring.security.jwt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Size(max = 80)
    private String email;

    @NotBlank
    @Size(max = 30)
    private String username;

    @NotBlank
    private String password;

    // EAGER: Carga todos los roles asociados al usuario
    // PERSIST: Graba los roles asociados al usuario, pero no los elimina
    @ManyToMany(fetch = FetchType.EAGER, targetEntity = RoleEntity.class, cascade = CascadeType.PERSIST)
    // user_role: Tabla intermedia entre usuarios y roles
    // joinColumns: Se indica el nombre que va a tener la clave foranea del usuario
    // inverseJoinColumns: Clave foranea del rol de la tabla generada
    // @JoinColumn: Clave foranea del usuario o rol de la tabla generada
    @JoinTable(name = "user_role",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name="role_id"))
    private Set<RoleEntity> roles;
}
