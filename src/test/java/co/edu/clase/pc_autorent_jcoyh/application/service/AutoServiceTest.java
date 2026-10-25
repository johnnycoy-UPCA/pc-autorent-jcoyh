package co.edu.clase.pc_autorent_jcoyh.application.service;

import co.edu.clase.pc_autorent_jcoyh.application.port.output.AlquilerRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.application.port.output.AutoRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Auto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AutoServiceTest {

    // 1. Declarar los componentes del laboratorio
    private AutoRepositoryPort autoRepositoryPortMock;
    private AlquilerRepositoryPort alquilerRepositoryPortMock;
    private AutoService autoService;

    @BeforeEach
    void setUp() {
        // 2. Crear los simuladores (Mocks) con Mockito
        autoRepositoryPortMock = Mockito.mock(AutoRepositoryPort.class);
        alquilerRepositoryPortMock = Mockito.mock(AlquilerRepositoryPort.class);

        // 3. Instanciar el servicio inyectándole los simuladores
        autoService = new AutoService(autoRepositoryPortMock, alquilerRepositoryPortMock);
    }

    @Test
    @DisplayName("Flujo Feliz: Debería permitir enviar el auto a mantenimiento cuando está libre y registrado")
    void deberiaPermitirMantenimientoCuandoAutoExisteYNoTieneAlquileres() {
        // === ARRANGE (Configurar el escenario simulado) ===
        String placa = "ABC-123";
        
        // Creamos un carro en estado DISPONIBLE en la memoria del simulador
        Auto autoSimulado = new Auto(1L, placa, "Mazda", "CX-30", 2026, 180000.0, 500000.0, 12500, "DISPONIBLE");

        // PROGRAMAMOS LOS MOCKS (Comportamiento de la base de datos falsa)
        // "Cuando busquen la placa ABC-123, devuelve de inmediato nuestro autoSimulado"
        when(autoRepositoryPortMock.buscarPorPlaca(placa)).thenReturn(Optional.of(autoSimulado));
        
        // "Cuando listen los alquileres del sistema, devuelve una lista vacía (no hay carros alquilados)"
        when(alquilerRepositoryPortMock.listarTodos()).thenReturn(Collections.emptyList());
        
        // "Cuando guarden el auto modificado, simplemente devuélvelo de regreso"
        when(autoRepositoryPortMock.guardar(any(Auto.class))).thenReturn(autoSimulado);

        // === ACT (Ejecutar la acción del servicio) ===
        // Intentamos meter el carro a taller pasándole enMantenimiento: true
        Auto autoResultado = autoService.actualizar(placa, true, 12500);

        // === ASSERT (Afirmar los resultados esperados) ===
        assertNotNull(autoResultado, "El auto devuelto no debería ser nulo");
        assertEquals("MANTENIMIENTO", autoResultado.getEstado(), "El estado del vehículo debería haber cambiado a MANTENIMIENTO");
        
        // CONTROL DE CALIDAD DE INFRAESTRUCTURA (Verificar que se guardaron los cambios)
        verify(autoRepositoryPortMock, times(1)).guardar(autoSimulado);
    }


    @Test
    @DisplayName("Regla de Oro: Debería lanzar RuntimeException al intentar enviar a taller un auto con Alquiler ACTIVO")
    void deberiaLanzarExcepcionAlIntentarMantenimientoSiElAutoTieneAlquilerActivo() {
        // === ARRANGE (Configurar el escenario de conflicto) ===
        String placa = "JCH-777";
        Long autoId = 1L;
        
        // Instanciamos el vehículo en el escenario
        Auto autoSimulado = new Auto(autoId, placa, "Mazda", "CX-30", 2026, 180000.0, 500000.0, 12500, "DISPONIBLE");

        // Creamos un contrato de alquiler ficticio que está atrapado en estado "ACTIVO"
        co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler alquilerActivo = new co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler();
        alquilerActivo.setAutoId(autoId);
        alquilerActivo.setEstado("ACTIVO");

        // PROGRAMAMOS LOS MOCKS
        // Cuando busquen la placa, devolvemos el auto
        when(autoRepositoryPortMock.buscarPorPlaca(placa)).thenReturn(Optional.of(autoSimulado));
        
        // Cuando listen los alquileres, Mockito devolverá el contrato ACTIVO que creamos
        java.util.List<co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler> listaConAlquiler = java.util.Collections.singletonList(alquilerActivo);
        when(alquilerRepositoryPortMock.listarTodos()).thenReturn(listaConAlquiler);

        // === ACT & ASSERT (Ejecutar y validar la explosión del escudo) ===
        // Afirmamos que el servicio debe arrojar una RuntimeException debido al contrato vigente
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            autoService.actualizar(placa, true, 12500);
        }, "Se esperaba que el orquestador bloqueara el envío a taller debido al alquiler activo");

        // Validamos que el mensaje interno expuesto sea el correcto
        assertTrue(excepcion.getMessage().contains("ACTIVO"), 
            "El mensaje de error debería reportar el conflicto con el alquiler activo");
            
        // CONTROL DE CALIDAD: Verificar que el repositorio de autos JAMÁS fue llamado para guardar cambios
        verify(autoRepositoryPortMock, never()).guardar(any(Auto.class));
    }


    @Test
    @DisplayName("Control de Catálogo: Debería lanzar RuntimeException si la placa no existe en el sistema")
    void deberiaLanzarExcepcionSiLaPlacaNoExisteEnElCatalogo() {
        // === ARRANGE (Configurar el escenario de ausencia de datos) ===
        String placaFalsa = "NON-999";
        
        // Programamos el simulador para que devuelva un Optional vacío (El carro no existe en H2)
        when(autoRepositoryPortMock.buscarPorPlaca(placaFalsa)).thenReturn(java.util.Optional.empty());

        // === ACT & ASSERT (Ejecutar y validar la respuesta del orquestador) ===
        RuntimeException excepcion = assertThrows(RuntimeException.class, () -> {
            autoService.actualizar(placaFalsa, true, 0);
        }, "Se esperaba que el servicio fallara de inmediato al no encontrar la placa");

        // Validamos el mensaje semántico definido en el servicio
        assertTrue(excepcion.getMessage().contains("no se encuentra registrado"), 
            "El mensaje debería indicar que el vehículo no está registrado");
            
        // CONTROL DE CALIDAD: Asegurar que el repositorio de alquileres JAMÁS fue consultado innecesariamente
        verify(alquilerRepositoryPortMock, never()).listarTodos();
    }

}
