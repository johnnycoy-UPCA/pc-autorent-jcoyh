package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto;

import java.time.LocalDate;

public class FinalizarAlquilerDto {
    private Double kilometrajeFinal;
    private LocalDate fechaFin;

    public Double getKilometrajeFinal() { 
        return kilometrajeFinal; 
    }
    public void setKilometrajeFinal(Double kilometrajeFinal) { 
        this.kilometrajeFinal = kilometrajeFinal; 
    }

    public LocalDate getFechaFin() { 
        return fechaFin; 
    }
    public void setFechaFin(LocalDate fechaFin) { 
        this.fechaFin = fechaFin; 
    }
}
