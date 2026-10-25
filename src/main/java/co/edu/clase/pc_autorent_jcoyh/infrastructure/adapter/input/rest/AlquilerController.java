package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest;

import co.edu.clase.pc_autorent_jcoyh.application.port.input.CancelarAlquilerUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.CrearAlquilerUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.FinalizarAlquilerUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.IniciarReservaUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.AlquilerRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.AlquilerException;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.MensajesError;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto.CrearAlquilerDto;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto.FinalizarAlquilerDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/alquileres")
public class AlquilerController {

    private final CrearAlquilerUseCase crearAlquilerUseCase;
    private final FinalizarAlquilerUseCase finalizarAlquilerUseCase;
    private final AlquilerRepositoryPort alquilerRepositoryPort;
    private final CancelarAlquilerUseCase cancelarAlquilerUseCase;
    private final IniciarReservaUseCase iniciarReservaUseCase;

    public AlquilerController(CrearAlquilerUseCase crearAlquilerUseCase, 
                            FinalizarAlquilerUseCase finalizarAlquilerUseCase, 
                            AlquilerRepositoryPort alquilerRepositoryPort,
                            CancelarAlquilerUseCase cancelarAlquilerUseCase,
                            IniciarReservaUseCase iniciarReservaUseCase) {
        this.crearAlquilerUseCase = crearAlquilerUseCase;
        this.finalizarAlquilerUseCase = finalizarAlquilerUseCase;
        this.alquilerRepositoryPort = alquilerRepositoryPort;
        this.cancelarAlquilerUseCase = cancelarAlquilerUseCase;
        this.iniciarReservaUseCase = iniciarReservaUseCase;
    }

    @PostMapping
    public ResponseEntity<Alquiler> crearAlquiler(@RequestBody CrearAlquilerDto request) {

        if (request.getClienteId() == null || 
            request.getAutoId() == null || 
            request.getCantidadDias() == null) {

            throw new AlquilerException(
                MensajesError.ALQUILER_CAMPOS_OBLIGATORIOS
            );
        }

    LocalDate fechaInicioCalculada = request.getFechaInicio() == null
                                    ? LocalDate.now() 
                                    : request.getFechaInicio();

        Alquiler alquilerSolicitud = new Alquiler(
                                        null, 
                                        request.getClienteId(), 
                                        request.getAutoId(), 
                                        null, 
                                        fechaInicioCalculada, 
                                        null, 
                                        request.getCantidadDias(), 
                                        0.0, 
                                        null, 
                                        null, 
                                        null
                                    );

        Alquiler nuevoAlquiler = crearAlquilerUseCase.crearAlquiler(alquilerSolicitud);
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(nuevoAlquiler);
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<Alquiler> iniciarReserva(@PathVariable("id") Long alquilerId) {
        
        Alquiler alquilerActivado = iniciarReservaUseCase.iniciarReserva(alquilerId);
        return ResponseEntity
            .ok(alquilerActivado);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Alquiler> finalizarAlquiler(@PathVariable("id") Long alquilerId, 
                                                    @RequestBody FinalizarAlquilerDto request) {
        
        if (request.getKilometrajeFinal() == null) {
            throw new AlquilerException(
                MensajesError.AUTO_NO_KILOMETRAJE
            );
        }

        LocalDate fechaEntregaReal = (request.getFechaFin() != null) 
                                    ? request.getFechaFin() 
                                    : LocalDate.now();

        Alquiler alquilerFinalizado = finalizarAlquilerUseCase.finalizarAlquiler(alquilerId, 
                                                                                request.getKilometrajeFinal(), 
                                                                                fechaEntregaReal);
        return ResponseEntity
            .ok(alquilerFinalizado);
    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<Alquiler> cancelarAlquiler(@PathVariable("id") Long alquilerId) {
        
        Alquiler alquilerCancelado = cancelarAlquilerUseCase.cancelarAlquiler(alquilerId);
        return ResponseEntity
            .ok(alquilerCancelado);
    }

    @GetMapping
    public ResponseEntity<List<Alquiler>> listarTodos() {
        return ResponseEntity
            .ok(alquilerRepositoryPort.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Alquiler> buscarPorId(@PathVariable("id") Long alquilerId) {
        return alquilerRepositoryPort.buscarPorId(alquilerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
