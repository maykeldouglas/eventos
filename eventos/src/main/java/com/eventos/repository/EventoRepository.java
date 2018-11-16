package com.eventos.repository;

import com.eventos.models.Evento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventoRepository extends JpaRepository<Evento, String> {

    Evento findByCodigo(long codigo);

}
