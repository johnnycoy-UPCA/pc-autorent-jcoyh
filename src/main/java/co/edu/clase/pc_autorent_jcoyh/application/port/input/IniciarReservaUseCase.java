package co.edu.clase.pc_autorent_jcoyh.application.port.input;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler;

public interface IniciarReservaUseCase {
    Alquiler iniciarReserva (Long alquilerId);
}
