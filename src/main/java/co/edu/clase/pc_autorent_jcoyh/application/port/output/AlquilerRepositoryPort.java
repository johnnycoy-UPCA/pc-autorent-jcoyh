package co.edu.clase.pc_autorent_jcoyh.application.port.output;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler;

import java.util.Optional;
import java.util.List;

public interface AlquilerRepositoryPort {
    Alquiler guardar(Alquiler alquiler);
    Optional<Alquiler> buscarPorId(Long alquilerId);
    List<Alquiler> listarTodos();
}
