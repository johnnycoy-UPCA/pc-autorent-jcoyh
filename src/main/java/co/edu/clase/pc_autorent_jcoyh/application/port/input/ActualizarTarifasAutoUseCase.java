package co.edu.clase.pc_autorent_jcoyh.application.port.input;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Auto;

public interface ActualizarTarifasAutoUseCase {
    Auto actualizarTarifasAuto(Long id, double nuevoPrecioPorDia, double nuevoDeposito);
}
