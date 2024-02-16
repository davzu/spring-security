package davzuzu.spring.security.jwt.model;

import jakarta.persistence.*;
import lombok.*;

@Data @AllArgsConstructor @NoArgsConstructor @Builder
@Entity
@Table(name = "rol")
public class RoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Enumerated: Asigna un enum a una columna de base de datos. Acepta dos tipos de mapeos.
    // El valor predeterminado es ORDINAL
    @Enumerated(EnumType.STRING)
    private ERole name;

}
