package co.edu.clase.pc_autorent_jcoyh.domain.model;

public class Cliente {
    private final Long id;
    private final String nombre;
    private final String tipoDocumento;
    private final Long documento;
    private String celular;
    private String email;
    private String estado;

    public Cliente(Long id, 
                String nombre, 
                String tipoDocumento, 
                Long documento, 
                String celular, 
                String email, 
                String estado) {
                      
        this.id = id;
        this.nombre = nombre;
        this.tipoDocumento = (tipoDocumento == null || tipoDocumento.isBlank())
                            ? "CC"
                            : tipoDocumento.trim();
        this.documento = documento;
        this.celular = celular;
        this.email = email;
        this.estado = estado != null 
                    ? estado 
                    : EstadosNegocio.CLIENTE_ACTIVO;
    }

    public void desactivarCliente() {
        this.estado = EstadosNegocio.CLIENTE_INACTIVO;
    }

    public  void reactivarCliente() {
        this.estado = EstadosNegocio.CLIENTE_ACTIVO;
    }

    public Long getId() { 
        return id; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public String getTipoDocumento() { 
        return tipoDocumento; 
    }

    public Long getDocumento() { 
        return documento; 
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

    public String getEstado() { 
        return estado; 
    }
    public void setEstado(String estado) { 
        this.estado = estado; 
    }
}
