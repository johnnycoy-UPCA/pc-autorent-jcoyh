package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
public class ClienteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(name = "tipo_documento", nullable = false, length = 20)
    private String tipoDocumento;

    @Column(nullable = false, unique = true)
    private Long documento;

    @Column(length = 20)
    private String celular;

    @Column(length = 100)
    private String email;

    @Column(nullable = false, length = 20)
    private String estado;

    public ClienteEntity() {}

    public ClienteEntity(Long id, 
                        String nombre, 
                        String tipoDocumento, 
                        Long documento, 
                        String celular, 
                        String email, 
                        String estado) {

        this.id = id;
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.documento = documento;
        this.celular = celular;
        this.email = email;
        this.estado = estado;
    }


    public Long getId() { 
        return id; 
    }
    public void setId(Long id) { 
        this.id = id; 
    }

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

    public Long getDocumento() { 
        return documento; 
    }
    public void setDocumento(Long documento) { 
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

    public String getEstado() { 
        return estado; 
    }
    public void setEstado(String estado) { 
        this.estado = estado; 
    }
}
