package co.edu.clase.pc_autorent_jcoyh.application.port.input;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Cliente;

public interface RegistrarClienteUseCase {
    Cliente registrarCliente(String nombre, 
                            String tipoDocumento, 
                            String documentoCrudo, 
                            String celular, 
                            String email);
}