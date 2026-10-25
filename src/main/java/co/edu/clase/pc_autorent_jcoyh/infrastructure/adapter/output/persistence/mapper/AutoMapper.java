package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.mapper;

import co.edu.clase.pc_autorent_jcoyh.domain.model.Auto;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity.AutoEntity;
import org.springframework.stereotype.Component;

@Component
public class AutoMapper {

    public Auto toDomain(AutoEntity entity) {
        if (entity == null) {
            return null;
        }

        return new Auto(
            entity.getId(),
            entity.getPlaca(),
            entity.getMarca(),
            entity.getModelo(),
            entity.getAnio(),
            entity.getPrecioPorDia(),
            entity.getDeposito(),
            entity.getKilometraje(),
            entity.getEstado()
        );
    }

    public AutoEntity toEntity(Auto domain) {
        if (domain == null) {
            return null;
        }
        
        return new AutoEntity(
            domain.getId(),
            domain.getPlaca(),
            domain.getMarca(),
            domain.getModelo(),
            domain.getAnio(),
            domain.getPrecioPorDia(),
            domain.getDeposito(),
            domain.getKilometraje(),
            domain.getEstado()
        );
    }
}
