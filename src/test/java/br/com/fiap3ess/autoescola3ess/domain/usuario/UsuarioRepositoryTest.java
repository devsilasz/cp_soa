package br.com.fiap3ess.autoescola3ess.domain.usuario;

import br.com.fiap3ess.autoescola3ess.application.core.domain.Usuario;
import br.com.fiap3ess.autoescola3ess.application.port.out.UsuarioRepository;
import br.com.fiap3ess.autoescola3ess.shared.vo.enumeration.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager testEntity;

    @Test
    @DisplayName("Expectativa: Retornar o usuário quando o login existir.")
    void findByLoginCenario1() {
        //arrange
        Usuario usuario = new Usuario(null, "usuario.teste", "senha-hash", Role.ADMIN);
        testEntity.persist(usuario);

        //act
        UserDetails encontrado = repository.findByLogin("usuario.teste");

        //assert
        assertThat(encontrado).isEqualTo(usuario);
        assertThat(encontrado.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    @DisplayName("Expectativa: Retornar null quando o login não existir.")
    void findByLoginCenario2() {
        //act
        UserDetails encontrado = repository.findByLogin("login.inexistente");

        //assert
        assertThat(encontrado).isNull();
    }
}
