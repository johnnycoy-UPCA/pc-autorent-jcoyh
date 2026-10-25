package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto;

public class ActualizarMantenimientoDto {
    private String placa;
    private Boolean enMantenimiento;
    private Double kilometraje;

    public String getPlaca() { 
        return placa; 
    }
    public void setPlaca(String placa) { 
        this.placa = placa; 
    }

    public Boolean getEnMantenimiento() { 
        return enMantenimiento; 
    }
    public void setEnMantenimiento(Boolean enMantenimiento) { 
        this.enMantenimiento = enMantenimiento; 
    }

    public Double getKilometraje() { 
        return kilometraje; 
    }
    public void setKilometraje(Double kilometraje) { 
        this.kilometraje = kilometraje; 
    }
}
