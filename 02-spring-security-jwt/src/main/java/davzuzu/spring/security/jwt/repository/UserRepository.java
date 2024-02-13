package davzuzu.spring.security.jwt.repository;

import davzuzu.spring.security.jwt.model.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Long> {

    // findBy es el indicador de JPA para buscar por el campo que continua después de la palabra findBy, de
    // esta forma no se tendra que hacer un query para buscar por el Username del usuario
    Optional<UserEntity> findByUsername(String username);

    // ?1 primer parametro que se encuentra dentro de los argumentos del metodo
    @Query("select u from UserEntity u where u.username = ?1")
    Optional<UserEntity> getName(String username);
}
