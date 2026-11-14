package com.taskflow.qa.infrastructure;

import com.taskflow.qa.domain.PerfilAcesso;
import com.taskflow.qa.persistence.entity.PerfilEntity;
import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.repository.PerfilRepository;
import com.taskflow.qa.persistence.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
class DemoAdminBootstrap {

    @Bean
    ApplicationRunner createDemoAdmin(
            UsuarioRepository usuarioRepository,
            PerfilRepository perfilRepository,
            PasswordEncoder passwordEncoder,
            @Value("${taskflow.demo-admin.email}") String email,
            @Value("${taskflow.demo-admin.password}") String password) {
        return args -> {
            for (PerfilAcesso role : PerfilAcesso.values()) {
                perfilRepository.findByNome(role)
                        .orElseGet(() -> perfilRepository.save(new PerfilEntity(role)));
            }

            if (usuarioRepository.findByEmailIgnoreCase(email).isPresent()) {
                return;
            }

            PerfilEntity adminProfile = perfilRepository.findByNome(PerfilAcesso.ADMINISTRADOR)
                    .orElseGet(() -> perfilRepository.save(new PerfilEntity(PerfilAcesso.ADMINISTRADOR)));
            UsuarioEntity admin = new UsuarioEntity("Administrador de demonstração", email,
                    passwordEncoder.encode(password));
            admin.adicionarPerfil(adminProfile);
            usuarioRepository.save(admin);
        };
    }
}
