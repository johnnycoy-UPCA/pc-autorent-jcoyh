package co.edu.clase.pc_autorent_jcoyh.application.port.output;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Auto;
import java.util.Optional;
import java.util.List;

public interface AutoRepositoryPort {
    Auto guardar(Auto auto);
    Optional<Auto> buscarPorId(Long autoId);
    List<Auto> buscarPorPlaca(String autoPlaca);
    List<Auto> listarTodos();
    List<Auto> buscarDisponibles();
}
