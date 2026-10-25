package co.edu.clase.pc_autorent_jcoyh.application.service;

import co.edu.clase.pc_autorent_jcoyh.application.port.output.AlquilerRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.ClienteRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.domain.exception.ClienteException;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Cliente;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ClienteServiceTest {

    private ClienteRepositoryPort clienteRepositoryPortMock;
    private AlquilerRepositoryPort alquilerRepositoryPortMock;
    private ClienteService clienteService;

    @BeforeEach
    void setUp() {
        clienteRepositoryPortMock = Mockito.mock(ClienteRepositoryPort.class);
        alquilerRepositoryPortMock = Mockito.mock(AlquilerRepositoryPort.class);
        clienteService = new ClienteService(clienteRepositoryPortMock, alquilerRepositoryPortMock);
    }

    @Test
    @DisplayName("Flujo 1 y 5: Permite registrar un cliente si no existe o si el existente está INACTIVO")
    void deberiaPermitirRegistrarClienteCuandoNoEstaActivo() {
        Cliente clienteNuevo = new Cliente(null, "Carlos Pérez", "CC", 1020304050L, "3001234567", "carlos@email.com", "ACTIVO");
        Cliente clienteInactivoHistorico = new Cliente(1L, "Carlos Pérez", "CC", 1020304050L, "3001234567", "carlos@email.com", "INACTIVO");

        when(clienteRepositoryPortMock.buscarPorDocumento(1020304050L)).thenReturn(Optional.of(clienteInactivoHistorico));
        when(clienteRepositoryPortMock.guardar(any(Cliente.class))).thenReturn(clienteNuevo);

        Cliente resultado = clienteService.registrar(clienteNuevo);
        assertNotNull(resultado);
        verify(clienteRepositoryPortMock, times(1)).guardar(any(Cliente.class));
    }

    @Test
    @DisplayName("Flujo 2 y 6: Lanza excepción si se intenta duplicar un cliente con estado ACTIVO")
    void deberiaLanzarExcepcionSiClienteYaExisteActivo() {
        Cliente clienteDuplicado = new Cliente(null, "Carlos Pérez", "CC", 1020304050L, "3001234567", "carlos@email.com", "ACTIVO");
        Cliente clienteActivoEnDb = new Cliente(1L, "Carlos Pérez", "CC", 1020304050L, "3001234567", "carlos@email.com", "ACTIVO");

        when(clienteRepositoryPortMock.buscarPorDocumento(1020304050L)).thenReturn(Optional.of(clienteActivoEnDb));

        assertThrows(ClienteException.class, () -> {
            clienteService.registrar(clienteDuplicado);
        });
        verify(clienteRepositoryPortMock, never()).guardar(any(Cliente.class));
    }

    @Test
    @DisplayName("Flujo 3: Permite dar de baja si el cliente no tiene alquileres activos")
    void deberiaDesactivarClienteCorrectamente() {
        Cliente clienteActivo = new Cliente(2L, "Carlos Pérez", "CC", 1020304050L, "3001234567", "carlos@email.com", "ACTIVO");

        when(clienteRepositoryPortMock.buscarPorId(2L)).thenReturn(Optional.of(clienteActivo));
        when(alquilerRepositoryPortMock.listarTodos()).thenReturn(Collections.emptyList());
        when(clienteRepositoryPortMock.guardar(any(Cliente.class))).thenReturn(clienteActivo);

        Cliente resultado = clienteService.darDeBaja(2L);
        assertEquals("INACTIVO", resultado.getEstado());
    }

    @Test
    @DisplayName("Flujo 4: Bloquea la baja si el cliente posee un contrato activo de alquiler")
    void deberiaBloquearBajaSiTieneAlquilerActivo() {
        Cliente clienteActivo = new Cliente(2L, "Carlos Pérez", "CC", 1020304050L, "3001234567", "carlos@email.com", "ACTIVO");
        Alquiler alquilerActivo = new Alquiler();
        alquilerActivo.setClienteId(2L);
        alquilerActivo.setEstado("ACTIVO");

        when(clienteRepositoryPortMock.buscarPorId(2L)).thenReturn(Optional.of(clienteActivo));
        when(alquilerRepositoryPortMock.listarTodos()).thenReturn(List.of(alquilerActivo));

        assertThrows(RuntimeException.class, () -> {
            clienteService.darDeBaja(2L);
        });
        verify(clienteRepositoryPortMock, never()).guardar(any(Cliente.class));
    }
}
