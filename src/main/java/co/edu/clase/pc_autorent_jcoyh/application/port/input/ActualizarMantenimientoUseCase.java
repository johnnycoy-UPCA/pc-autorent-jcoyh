package co.edu.clase.pc_autorent_jcoyh.application.port.input;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Auto;

public interface ActualizarMantenimientoUseCase {
    Auto actualizarMantenimiento(String placa, 
                                boolean enMantenimiento, 
                                Double kilometraje);
}
