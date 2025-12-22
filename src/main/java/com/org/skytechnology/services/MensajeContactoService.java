package com.org.skytechnology.services;

import com.org.skytechnology.entity.MensajeContacto;
import java.util.List;

public interface MensajeContactoService {

    MensajeContacto guardar(MensajeContacto mensaje);

    List<MensajeContacto> listarTodos();

    MensajeContacto obtenerPorId(Long id);

    void eliminar(Long id);

    long contarMensajes();
}
