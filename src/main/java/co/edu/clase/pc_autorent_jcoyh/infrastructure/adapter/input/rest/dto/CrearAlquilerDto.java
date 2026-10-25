package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto;

import java.time.LocalDate;

public class CrearAlquilerDto {
    private Long clienteId;
    private Long autoId;
    private Integer cantidadDias;
    private LocalDate fechaInicio;

    public Long getClienteId() { 
        return clienteId; 
    }
    public void setClienteId(Long clienteId) { 
        this.clienteId = clienteId; 
    }
    
    public Long getAutoId() { 
        return autoId; 
    }
    public void setAutoId(Long autoId) { 
        this.autoId = autoId; 
    }
    
    public Integer getCantidadDias() { 
        return cantidadDias; 
    }
    public void setCantidadDias(Integer cantidadDias) { 
        this.cantidadDias = cantidadDias; 
    }
    
    public LocalDate getFechaInicio() { 
        return fechaInicio; 
    }
    public void setFechaInicio(LocalDate fechaInicio) { 
        this.fechaInicio = fechaInicio; 
    }
}
