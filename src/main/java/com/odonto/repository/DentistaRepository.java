package com.odonto.repository;

import com.odonto.model.Usuario;
import com.odonto.model.Dentista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistaRepository extends JpaRepository<Dentista, Long> {

    Boolean existsByUsuario(Dentista professor);

    Dentista findByUsuario(Usuario usuario);
}
