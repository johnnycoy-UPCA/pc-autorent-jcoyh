package co.edu.clase.pc_autorent_jcoyh.application.port.input;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Cliente;

public interface ActualizarClienteUseCase {
    Cliente actualizarCliente(Long id,
                            String nuevoTelefono, 
                            String nuevoEmail);
}
