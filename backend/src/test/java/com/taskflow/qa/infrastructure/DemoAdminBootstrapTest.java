package com.taskflow.qa.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.taskflow.qa.domain.PerfilAcesso;
import com.taskflow.qa.persistence.repository.UsuarioRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@ActiveProfiles("dev")
@Transactional
class DemoAdminBootstrapTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void createsLocalDemoAdminWithEncodedPasswordAndAdministratorProfile() {
        var admin = usuarioRepository.findByEmailIgnoreCase("admin@taskflow.local").orElseThrow();

        assertTrue(passwordEncoder.matches("TaskFlow-Demo-2026!", admin.getSenhaHash()));
        assertEquals(1, admin.getPerfis().size());
        assertEquals(PerfilAcesso.ADMINISTRADOR, admin.getPerfis().getFirst().getPerfil().getNome());
    }
}
