package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "autos")
public class AutoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 7)
    private String placa;

    @Column(nullable = false, length = 50)
    private String marca;

    @Column(nullable = false, length = 50)
    private String modelo;

    @Column(nullable = false)
    private int anio;

    @Column(name = "precio_por_dia", nullable = false)
    private double precioPorDia;

    @Column(nullable = false)
    private double deposito;

    @Column(nullable = false)
    private Double kilometraje;

    @Column(nullable = false, length = 20)
    private String estado;

    public AutoEntity() {}

    public AutoEntity(Long id, 
                    String placa, 
                    String marca, 
                    String modelo, 
                    int anio, 
                    double precioPorDia, 
                    double deposito, 
                    Double kilometraje, 
                    String estado) {
                        
        this.id = id;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precioPorDia = precioPorDia;
        this.deposito = deposito;
        this.kilometraje = kilometraje;
        this.estado = estado;
    }

    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

    public String getPlaca() { 
        return placa; 
    }
    public void setPlaca(String placa) { 
        this.placa = placa; 
    }

    public String getMarca() { 
        return marca; 
    }
    public void setMarca(String marca) { 
        this.marca = marca; 
    }

    public String getModelo() { 
        return modelo; 
    }
    public void setModelo(String modelo) { 
        this.modelo = modelo; 
    }

    public int getAnio() { 
        return anio; 
    }
    public void setAnio(int anio) { 
        this.anio = anio; 
    }

    public double getPrecioPorDia() { 
        return precioPorDia; 
    }
    public void setPrecioPorDia(double precioPorDia) { 
        this.precioPorDia = precioPorDia; 
    }

    public double getDeposito() { 
        return deposito; 
    }
    public void setDeposito(double deposito) { 
        this.deposito = deposito; 
    }

    public Double getKilometraje() { 
        return kilometraje; 
    }
    public void setKilometraje(Double kilometraje) { 
        this.kilometraje = kilometraje; 
    }

    public String getEstado() { 
        return estado; 
    }
    public void setEstado(String estado) { 
        this.estado = estado; 
    }
}
