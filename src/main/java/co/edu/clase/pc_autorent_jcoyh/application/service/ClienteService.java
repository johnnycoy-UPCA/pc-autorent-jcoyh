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

    @Override
    public Cliente registrarCliente(Cliente cliente) {

        if (cliente.getNombre() == null || cliente.getNombre().isBlank()) {
            throw new ClienteException(MensajesError.CLIENTE_CAMPO_OBLIGATORIO);
        }
        if (cliente.getDocumento() == null || cliente.getDocumento() <= 0) {
            throw new ClienteException(MensajesError.CLIENTE_DOCUMENTO_OBLIGATORIO);
        }
        if (cliente.getEmail() == null || !cliente.getEmail().contains("@")) {
            throw new ClienteException(String.format(MensajesError.CLIENTE_EMAIL_INVALIDO, cliente.getEmail()));
        }

        Optional<Cliente> clienteExistente = clienteRepositoryPort.buscarPorDocumento(cliente.getDocumento());
        
        if (clienteExistente.isPresent()) {
            Cliente encontrado = clienteExistente.get();
                throw new ClienteException(
                    String.format(MensajesError.CLIENTE_DOCUMENTO_REPETIDO, encontrado.getDocumento())
                );
        }

        Cliente nuevoCliente = new Cliente(
                                    null,
                                    cliente.getNombre(),
                                    cliente.getTipoDocumento(),
                                    cliente.getDocumento(),
                                    cliente.getCelular(),
                                    cliente.getEmail(),
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
            if (!nuevoEmail.contains("@")) {
                throw new ClienteException(
                    String.format(MensajesError.CLIENTE_EMAIL_INVALIDO, nuevoEmail)
                );
            }
        }
        
        cliente.setCelular(nuevoTelefono == null ? cliente.getCelular() : nuevoTelefono);
        cliente.setEmail(nuevoEmail == null ? cliente.getEmail() : nuevoEmail);

        return clienteRepositoryPort.guardar(cliente);
    }
}
