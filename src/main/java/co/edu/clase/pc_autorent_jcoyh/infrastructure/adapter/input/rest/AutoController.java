package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest;

import co.edu.clase.pc_autorent_jcoyh.application.port.input.RegistrarAutoUseCase;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Auto;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto.ActualizarMantenimientoDto;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto.CrearAutoDto;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.ActualizarMantenimientoUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.input.DarBajaAutoUseCase;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.AutoRepositoryPort;

import java.util.List;

@RestController
@RequestMapping("/api/autos")
public class AutoController {

    private final RegistrarAutoUseCase registrarAutoUseCase;
    private final AutoRepositoryPort autoRepositoryPort;
    private final ActualizarMantenimientoUseCase actualizarMantenimientoUseCase;
    private final DarBajaAutoUseCase darBajaAutoUseCase;

    public AutoController(RegistrarAutoUseCase registrarAutoUseCase, 
                        AutoRepositoryPort autoRepositoryPort,
                        ActualizarMantenimientoUseCase actualizarMantenimientoUseCase,
                        DarBajaAutoUseCase darBajaAutoUseCase) {
        this.registrarAutoUseCase = registrarAutoUseCase;
        this.autoRepositoryPort = autoRepositoryPort;
        this.actualizarMantenimientoUseCase = actualizarMantenimientoUseCase;
        this.darBajaAutoUseCase = darBajaAutoUseCase;
    }

    @PostMapping
    public ResponseEntity<Auto> registrarAuto(@RequestBody CrearAutoDto request) {
        Double kilometrajeInicial = (request.getKilometraje() == null) ? 0.0 : request.getKilometraje();

        Auto autoSolicitud = new Auto(
            null, 
            request.getPlaca(), 
            request.getMarca(), 
            request.getModelo(), 
            request.getAnio(), 
            request.getPrecioPorDia(), 
            request.getDeposito(), 
            kilometrajeInicial, 
            null
        );

        Auto nuevoAuto = registrarAutoUseCase.registrarAuto(autoSolicitud);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(nuevoAuto);
    }

    @PutMapping("/mantenimiento")
    public ResponseEntity<Auto> actualizarMantenimiento(@RequestBody ActualizarMantenimientoDto request) {

        String placa = request.getPlaca().trim();
        boolean enMantenimiento = request.getEnMantenimiento();
        Double kilometraje = request.getKilometraje();

        Auto autoMantenimiento = actualizarMantenimientoUseCase.actualizarMantenimiento(placa, 
                                    enMantenimiento, 
                                    kilometraje);
        return ResponseEntity
            .ok(autoMantenimiento);
    }

    @PutMapping("/{id}/desactivar")
    public ResponseEntity<Auto> darDeBajaAuto(@PathVariable("id") Long autoId) {

        Auto autoDesactivado = darBajaAutoUseCase.darDeBajaAuto(autoId);
        return ResponseEntity
            .ok(autoDesactivado);
    }

    @GetMapping
    public ResponseEntity<List<Auto>> listarTodos() {
        return ResponseEntity
            .ok(autoRepositoryPort.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Auto> buscarPorId(@PathVariable("id") Long autoId) {
        return autoRepositoryPort.buscarPorId(autoId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}