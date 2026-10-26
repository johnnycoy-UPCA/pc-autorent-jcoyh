package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.input.rest.dto;

public class CrearClienteDto {
    private String nombre;
    private String tipoDocumento;
    private String documento;
    private String celular;
    private String email;

    public CrearClienteDto() {}

    public String getNombre() { 
        return nombre; 
    }
    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getTipoDocumento() { 
        return tipoDocumento; 
    }
    public void setTipoDocumento(String tipoDocumento) { 
        this.tipoDocumento = tipoDocumento; 
    }

    public String getDocumento() { 
        return documento; 
    }
    public void setDocumento(String documento) { 
        this.documento = documento; 
    }

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
