package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest;

import co.edu.clase.pc_autorent_jcoyh.application.port.input.DarBajaClienteUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.ActualizarClienteUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.RegistrarClienteUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.ClienteRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Cliente;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto.ActualizarClienteDto;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto.CrearClienteDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final RegistrarClienteUseCase registrarClienteUseCase;
    private final ClienteRepositoryPort clienteRepositoryPort;
    private final DarBajaClienteUseCase darBajaClienteUseCase;
    private final ActualizarClienteUseCase actualizarClienteUseCase;

    public ClienteController(RegistrarClienteUseCase registrarClienteUseCase, 
                            ClienteRepositoryPort clienteRepositoryPort, 
                            DarBajaClienteUseCase darBajaClienteUseCase,
                            ActualizarClienteUseCase actualizarClienteUseCase) {

        this.registrarClienteUseCase = registrarClienteUseCase;
        this.clienteRepositoryPort = clienteRepositoryPort;
        this.darBajaClienteUseCase = darBajaClienteUseCase;
        this.actualizarClienteUseCase = actualizarClienteUseCase;
    }

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody CrearClienteDto request) {
        Cliente clienteRegistrado = registrarClienteUseCase.registrarCliente(request.getNombre(), 
                                                                            request.getTipoDocumento(), 
                                                                            request.getDocumento(), 
                                                                            request.getCelular(), 
                                                                            request.getEmail());
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(clienteRegistrado);
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Cliente> darDeBajaCliente(@PathVariable("id") Long clienteId) {
        Cliente clienteDesactivado = darBajaClienteUseCase.darDeBajaCliente(clienteId);
        return ResponseEntity
            .ok(clienteDesactivado);
    }

    @PutMapping("/{id}/actualizar")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable("id") Long clienteId, 
                                                    @RequestBody ActualizarClienteDto request) {
        Cliente clienteActualizado = actualizarClienteUseCase.actualizarCliente(clienteId,
                                                                                request.getCelular(),
                                                                                request.getEmail()
        );
        return ResponseEntity
            .ok(clienteActualizado);
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        return ResponseEntity
            .ok(clienteRepositoryPort.listarTodos()); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@PathVariable("id") Long clienteId) {
        return clienteRepositoryPort.buscarPorId(clienteId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}