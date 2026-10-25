package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.adapter;

import co.edu.clase.pc_autorent_jcoyh.application.port.output.AlquilerRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Alquiler;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity.AlquilerEntity;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.mapper.AlquilerMapper;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.repository.AlquilerJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;

@Component
public class AlquilerPersistenceAdapter implements AlquilerRepositoryPort {

    private final AlquilerJpaRepository alquilerJpaRepository;
    private final AlquilerMapper alquilerMapper;

    public AlquilerPersistenceAdapter(AlquilerJpaRepository alquilerJpaRepository, 
                                    AlquilerMapper alquilerMapper) {

        this.alquilerJpaRepository = alquilerJpaRepository;
        this.alquilerMapper = alquilerMapper;
    }

    @Override
    public Alquiler guardar(Alquiler alquiler) {
        AlquilerEntity entity = alquilerMapper.toEntity(alquiler);
        AlquilerEntity entityGuardada = alquilerJpaRepository.save(entity);
        return alquilerMapper.toDomain(entityGuardada);
    }

    @Override
    public Optional<Alquiler> buscarPorId(Long alquilerId) {
        return alquilerJpaRepository.findById(alquilerId).map(alquilerMapper::toDomain);
    }

    public List<Alquiler> listarTodos() {
        return alquilerJpaRepository
                .findAll()
                .stream()
                .map(alquilerMapper::toDomain)
                .toList();
    }    

}
