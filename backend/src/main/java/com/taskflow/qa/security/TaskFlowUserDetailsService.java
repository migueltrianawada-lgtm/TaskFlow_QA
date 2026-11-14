package com.taskflow.qa.security;

import com.taskflow.qa.persistence.entity.UsuarioEntity;
import com.taskflow.qa.persistence.repository.UsuarioRepository;
import java.util.Locale;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TaskFlowUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public TaskFlowUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UsuarioEntity usuario = usuarioRepository.findByEmailIgnoreCase(email.trim().toLowerCase(Locale.ROOT))
                .orElseThrow(() -> new UsernameNotFoundException("Credenciais inválidas."));
        return User.withUsername(usuario.getEmail())
                .password(usuario.getSenhaHash())
                .disabled(!usuario.isAtivo())
                .authorities(usuario.getPerfis().stream()
                        .map(usuarioPerfil -> new SimpleGrantedAuthority(
                                "ROLE_" + usuarioPerfil.getPerfil().getNome().name()))
                        .toList())
                .build();
    }
}
