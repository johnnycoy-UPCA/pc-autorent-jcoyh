package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto;

public class ActualizarTarifasAutoDto {
    private double precioPorDia;
    private double deposito;

    public ActualizarTarifasAutoDto() {}

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
}
