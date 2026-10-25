package co.edu.clase.pc_autorent_jcoyh.domain.model;

import java.time.LocalDate;

public class Alquiler {
    private final Long id;
    private final Long clienteId;
    private final Long autoId;
    private final LocalDate fechaReserva;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cantidadDias;
    private double valorTotal;
    private Double kilometrajeInicial;
    private Double kilometrajeFinal;
    private String estado;

    public Alquiler(Long id, 
                    Long clienteId, 
                    Long autoId, 
                    LocalDate fechaReserva, 
                    LocalDate fechaInicio, 
                    LocalDate fechaFin, 
                    int cantidadDias, 
                    double valorTotal, 
                    Double kilometrajeInicial, 
                    Double kilometrajeFinal, 
                    String estado) {

        this.id = id;
        this.clienteId = clienteId;
        this.autoId = autoId;
        this.fechaReserva = fechaReserva != null 
                            ? fechaReserva 
                            : LocalDate.now();
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadDias = cantidadDias;
        this.valorTotal = valorTotal;
        this.kilometrajeInicial = kilometrajeInicial;
        this.kilometrajeFinal = kilometrajeFinal;
        this.estado = estado != null 
                    ? estado 
                    : EstadosNegocio.ALQUILER_PENDIENTE;
    }

    public Long getId() { 
        return id; 
    }

    public Long getClienteId() { 
        return clienteId; 
    }

    public Long getAutoId() { 
        return autoId; 
    }

    public LocalDate getFechaReserva() { 
        return fechaReserva; 
    }

    public LocalDate getFechaInicio() { 
        return fechaInicio; 
    }
    public void setFechaInicio(LocalDate fechaInicio) { 
        this.fechaInicio = fechaInicio; 
    }

    public LocalDate getFechaFin() { 
        return fechaFin; 
    }
    public void setFechaFin(LocalDate fechaFin) { 
        this.fechaFin = fechaFin; 
    }

    public int getCantidadDias() { 
        return cantidadDias; 
    }
    public void setCantidadDias(int cantidadDias) { 
        this.cantidadDias = cantidadDias; 
    }

    public double getValorTotal() { 
        return valorTotal; 
    }
    public void setValorTotal(double valorTotal) { 
        this.valorTotal = valorTotal; 
    }

    public Double getKilometrajeInicial() { 
        return kilometrajeInicial; 
    }
    public void setKilometrajeInicial(Double kilometrajeInicial) { 
        this.kilometrajeInicial = kilometrajeInicial; 
    }

    public Double getKilometrajeFinal() { 
        return kilometrajeFinal; 
    }
    public void setKilometrajeFinal(Double kilometrajeFinal) { 
        this.kilometrajeFinal = kilometrajeFinal; 
    }

    public String getEstado() { 
        return estado; 
    }
    public void setEstado(String estado) { 
        this.estado = estado; 
    }
}
