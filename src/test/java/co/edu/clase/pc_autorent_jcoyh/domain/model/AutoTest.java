package co.edu.clase.pc_autorent_jcoyh.domain.model;

import co.edu.clase.pc_autorent_jcoyh.domain.exception.AutoException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AutoTest {

    @Test
    @DisplayName("Debería lanzar AutoNoDisponibleException cuando el vehículo no está DISPONIBLE para taller")
    void deberiaLanzarExcepcionAlIngresarAMantenimientoSiNoEstaDisponible() {
        // 1. Cargar datos en memoria
        Auto auto = new Auto();
        auto.setPlaca("ABC-123");
        auto.setEstado("ALQUILADO"); // Forzamos el estado conflictivo

        // 2. Llamo a la excepción para confirmar su comportamiento
        assertThrows(AutoException.class, () -> {
            auto.ingresarAMantenimiento();
        }, "Se esperaba que el dominio rechazara el ingreso a taller de un carro alquilado");
    }
}
