package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto;

public class CrearAutoDto {
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private double precioPorDia;
    private double deposito;
    private Double kilometraje;

    public CrearAutoDto() {}

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

    public Integer getAnio() { 
        return anio; 
    }
    public void setAnio(Integer anio) { 
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
}
