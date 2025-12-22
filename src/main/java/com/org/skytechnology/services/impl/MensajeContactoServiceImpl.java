package com.org.skytechnology.services.impl;

import com.org.skytechnology.entity.MensajeContacto;
import com.org.skytechnology.repositories.MensajeContactoRepository;
import com.org.skytechnology.services.MensajeContactoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MensajeContactoServiceImpl implements MensajeContactoService {

    private final MensajeContactoRepository mensajeContactoRepository;

    @Override
    @Transactional
    public MensajeContacto guardar(MensajeContacto mensaje) {
        if (mensaje.getFechaEnvio() == null) {
            mensaje.setFechaEnvio(LocalDateTime.now());
        }
        log.info("Registrando nuevo mensaje de contacto: {}", mensaje.getCorreo());
        return mensajeContactoRepository.save(mensaje);
    }

    @Override
    @Transactional(readOnly = true)
    public List<MensajeContacto> listarTodos() {
        return mensajeContactoRepository.findAllByOrderByFechaEnvioDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public MensajeContacto obtenerPorId(Long id) {
        return mensajeContactoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("El mensaje con ID " + id + " no existe."));
    }

    @Override
    @Transactional
    public void eliminar(Long id) {
        if (!mensajeContactoRepository.existsById(id)) {
            log.error("Intento de eliminar mensaje inexistente: ID {}", id);
            throw new RuntimeException("Mensaje no encontrado para eliminación");
        }
        mensajeContactoRepository.deleteById(id);
        log.info("Mensaje con ID {} eliminado correctamente", id);
    }

    @Override
    @Transactional(readOnly = true)
    public long contarMensajes() {
        return mensajeContactoRepository.count();
    }
}