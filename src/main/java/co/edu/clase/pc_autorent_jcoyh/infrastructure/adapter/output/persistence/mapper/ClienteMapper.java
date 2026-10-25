package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.mapper;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Cliente;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity.ClienteEntity;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toDomain(ClienteEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Cliente(
            entity.getId(),
            entity.getNombre(),
            entity.getTipoDocumento(),
            entity.getDocumento(),
            entity.getCelular(),
            entity.getEmail(),
            entity.getEstado()
        );
    }

    public ClienteEntity toEntity(Cliente domain) {
        if (domain == null) {
            return null;
        }
        
        return new ClienteEntity(
            domain.getId(),
            domain.getNombre(),
            domain.getTipoDocumento(),
            domain.getDocumento(),
            domain.getCelular(),
            domain.getEmail(),
            domain.getEstado()
        );
    }
}
