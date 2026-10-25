package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.mapper;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity.AlquilerEntity;
import org.springframework.stereotype.Component;

@Component
public class AlquilerMapper {

    public Alquiler toDomain(AlquilerEntity entity) {
        if (entity == null) {
            return null;
        }
        
        return new Alquiler(
            entity.getId(),
            entity.getClienteId(),
            entity.getAutoId(),
            entity.getFechaReserva(),
            entity.getFechaInicio(),
            entity.getFechaFin(),
            entity.getCantidadDias(),
            entity.getValorTotal(),
            entity.getKilometrajeInicial(),
            entity.getKilometrajeFinal(),
            entity.getEstado()
        );
    }

    public AlquilerEntity toEntity(Alquiler domain) {
        if (domain == null) {
            return null;
        }
        
        return new AlquilerEntity(
            domain.getId(),
            domain.getClienteId(),
            domain.getAutoId(),
            domain.getFechaReserva(),
            domain.getFechaInicio(),
            domain.getFechaFin(),
            domain.getCantidadDias(),
            domain.getValorTotal(),
            domain.getKilometrajeInicial(),
            domain.getKilometrajeFinal(),
            domain.getEstado()
        );
    }
}
