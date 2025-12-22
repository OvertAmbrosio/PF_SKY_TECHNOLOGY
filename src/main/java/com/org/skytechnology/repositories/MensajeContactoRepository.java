package com.org.skytechnology.repositories;

import com.org.skytechnology.entity.MensajeContacto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MensajeContactoRepository extends JpaRepository<MensajeContacto, Long> {

    List<MensajeContacto> findAllByOrderByFechaEnvioDesc();

    List<MensajeContacto> findByCorreoContainingIgnoreCase(String correo);

    Page<MensajeContacto> findAll(Pageable pageable);
}