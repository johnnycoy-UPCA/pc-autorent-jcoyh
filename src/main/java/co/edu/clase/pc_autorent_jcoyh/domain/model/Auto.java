package co.edu.clase.pc_autorent_jcoyh.domain.model;

public class Auto {
    private final Long id;
    private final String placa;
    private final String marca;
    private final String modelo;
    private final int anio;

    private double precioPorDia;
    private double deposito;
    private Double kilometraje;
    private String estado;


    public Auto(Long id, 
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
        this.kilometraje = kilometraje != null 
                        ? kilometraje 
                        : 0.0;
        this.estado = estado != null 
                    ? estado 
                    : EstadosNegocio.AUTO_DISPONIBLE;

    }

    public Long getId() { 
        return id; 
    }
    
    public String getPlaca() { 
        return placa; 
    }
    
    public String getMarca() { 
        return marca; 
    }
    
    public String getModelo() { 
        return modelo; 
    }
    
    public int getAnio() { 
        return anio; 
    }
    
    public double getPrecioPorDia() { 
        return precioPorDia; 
    }
    
    public double getDeposito() { 
        return deposito; 
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
