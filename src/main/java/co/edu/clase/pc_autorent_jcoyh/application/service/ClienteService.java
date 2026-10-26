package co.edu.clase.pc_autorent_jcoyh.application.service;

import co.edu.clase.pc_autorent_jcoyh.application.port.input.RegistrarClienteUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.ActualizarClienteUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.DarBajaClienteUseCase;

import co.edu.clase.pc_autorent_jcoyh.application.port.output.AlquilerRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.ClienteRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Cliente;
import co.edu.clase.pc_autorent_jcoyh.domain.model.EstadosNegocio;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.ClienteException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.NoEncontradoException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.MensajesError;

import java.util.Optional;

public class ClienteService implements RegistrarClienteUseCase, 
                                        DarBajaClienteUseCase,
                                        ActualizarClienteUseCase {

    private final ClienteRepositoryPort clienteRepositoryPort;
    private final AlquilerRepositoryPort alquilerRepositoryPort;

    public ClienteService(ClienteRepositoryPort clienteRepositoryPort, 
                        AlquilerRepositoryPort alquilerRepositoryPort) {

        this.clienteRepositoryPort = clienteRepositoryPort;
        this.alquilerRepositoryPort = alquilerRepositoryPort;
    }

    private void validarFormatoEmail(String email) {
        String regexRFC5322 = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+(?:\\.[a-zA-Z0-9_!#$%&'*+/=?`{|}~^-]+)*"
                            + "@" 
                            + "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

        if (!email.matches(regexRFC5322)) {
            throw new ClienteException(
                String.format(MensajesError.CLIENTE_EMAIL_INVALIDO, email)
            );
        }
    }

    private void validarFormatoCelular(String celular) {
        String regexCelular = "^3[0-9]{9}$";

        if (!celular.matches(regexCelular)) {
            throw new ClienteException(MensajesError.CLIENTE_CELULAR_INVALIDO);
        }
    }

    @Override
    public Cliente registrarCliente(String nombre, 
                                    String tipoDocumento, 
                                    String documentoCrudo, 
                                    String celular, 
                                    String email) {

        if (nombre == null || 
            documentoCrudo == null || 
            email == null ||
            celular == null || 
            nombre.isBlank() || 
            email.isBlank() ||
            celular.isBlank() ||
            documentoCrudo.isBlank()
        ) {    
            throw new ClienteException(MensajesError.CLIENTE_CAMPO_OBLIGATORIO);
        }
        if (!documentoCrudo.matches("^[0-9]+$")) {
            throw new ClienteException(MensajesError.CLIENTE_DOCUMENTO_VALIDO);
        }

        Long documentoNumerico = Long.parseLong(documentoCrudo);

        if (documentoNumerico <= 0) {
            throw new ClienteException(MensajesError.CLIENTE_DOCUMENTO_VALIDO);
        }
        validarFormatoEmail(email);
        validarFormatoCelular(celular);

        Optional<Cliente> clienteExistente = clienteRepositoryPort.buscarPorDocumento(documentoNumerico);
        
        if (clienteExistente.isPresent()) {
                throw new ClienteException(
                    String.format(MensajesError.CLIENTE_DOCUMENTO_REPETIDO, documentoNumerico)
                );
        }

        Cliente nuevoCliente = new Cliente(
                                    null,
                                    nombre,
                                    tipoDocumento,
                                    documentoNumerico,
                                    celular,
                                    email.trim().toLowerCase(),
                                    EstadosNegocio.CLIENTE_ACTIVO
                                );

        return clienteRepositoryPort.guardar(nuevoCliente);
    }

    @Override
    public Cliente darDeBajaCliente(Long id) {
        Cliente cliente = clienteRepositoryPort.buscarPorId(id)
            .orElseThrow(() -> new NoEncontradoException(
                    String.format(MensajesError.CLIENTE_NO_REGISTRADO, id)
                )
            );

        boolean tieneAlquilerActivo = alquilerRepositoryPort
                                        .listarTodos()
                                        .stream()
                                        .anyMatch(a -> a.getClienteId().equals(cliente.getId()) && 
                                                EstadosNegocio.ALQUILER_ACTIVO.equalsIgnoreCase(a.getEstado())
                                        );

        if (tieneAlquilerActivo) {
            throw new ClienteException(
                String.format(MensajesError.CLIENTE_ALQUILER_ACTIVO, cliente.getNombre())
            );
        }

        cliente.desactivarCliente();
        return clienteRepositoryPort.guardar(cliente);
    }

    @Override 
    public Cliente actualizarCliente(Long id,
                                    String nuevoTelefono, 
                                    String nuevoEmail) {

        Cliente cliente = clienteRepositoryPort.buscarPorId(id)
            .orElseThrow(() -> new NoEncontradoException(
                    String.format(MensajesError.CLIENTE_NO_REGISTRADO, id)
                )
            );                                        

        if (nuevoEmail != null) {
            validarFormatoEmail(nuevoEmail);
        }
        
        cliente.setCelular(nuevoTelefono == null ? cliente.getCelular() : nuevoTelefono);
        cliente.setEmail(nuevoEmail == null ? cliente.getEmail() : nuevoEmail.trim().toLowerCase());

        if (EstadosNegocio.CLIENTE_INACTIVO.equalsIgnoreCase(cliente.getEstado())) {
            cliente.setEstado(EstadosNegocio.CLIENTE_ACTIVO);
        }

        return clienteRepositoryPort.guardar(cliente);
    }
}
