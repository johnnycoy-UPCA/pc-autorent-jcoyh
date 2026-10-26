package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto;

public class ActualizarClienteDto {
    private String celular;
    private String email;

    public ActualizarClienteDto() {}

    public String getCelular() { 
        return celular; 
    }
    public void setCelular(String celular) { 
        this.celular = celular; 
    }
    
    public String getEmail() { 
        return email; 
    }
    public void setEmail(String email) { 
        this.email = email; 
    }
}
