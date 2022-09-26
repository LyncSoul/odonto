package com.odonto.repository;

import java.util.Optional;

import com.odonto.model.Usuario;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);

    Boolean existsByLogin(String login);

}
