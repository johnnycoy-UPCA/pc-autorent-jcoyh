package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "alquileres")
public class AlquilerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cliente_id", nullable = false)
    private Long clienteId;

    @Column(name = "auto_id", nullable = false)
    private Long autoId;

    @Column(name = "fecha_reserva", nullable = false)
    private LocalDate fechaReserva;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "cantidad_dias", nullable = false)
    private int cantidadDias;

    @Column(name = "valor_total", nullable = false)
    private double valorTotal;

    @Column(name = "kilometraje_inicial", nullable = true)
    private Double kilometrajeInicial;

    @Column(name = "kilometraje_final", nullable = true)
    private Double kilometrajeFinal;

    @Column(nullable = false, length = 20)
    private String estado;

    public AlquilerEntity() {}

    public AlquilerEntity(Long id, 
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
        this.fechaReserva = fechaReserva;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cantidadDias = cantidadDias;
        this.valorTotal = valorTotal;
        this.kilometrajeInicial = kilometrajeInicial;
        this.kilometrajeFinal = kilometrajeFinal;
        this.estado = estado;
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

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

    public LocalDate getFechaReserva() { 
        return fechaReserva; 
    }
    public void setFechaReserva(LocalDate fechaReserva) { 
        this.fechaReserva = fechaReserva; 
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
