package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.adapter;

import co.edu.clase.pc_autorent_jcoyh.application.port.output.AutoRepositoryPort;
import co.edu.clase.pc_autorent_jcoyh.domain.model.Auto;
import co.edu.clase.pc_autorent_jcoyh.domain.model.EstadosNegocio;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity.AutoEntity;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.mapper.AutoMapper;
import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.repository.AutoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.List;

@Component
public class AutoPersistenceAdapter implements AutoRepositoryPort {

    private final AutoJpaRepository autoJpaRepository;
    private final AutoMapper autoMapper;

    public AutoPersistenceAdapter(AutoJpaRepository autoJpaRepository, 
                                AutoMapper autoMapper) {
        this.autoJpaRepository = autoJpaRepository;
        this.autoMapper = autoMapper;
    }

    @Override
    public Auto guardar(Auto auto) {
        AutoEntity entity = autoMapper.toEntity(auto);
        AutoEntity entityGuardada = autoJpaRepository.save(entity);
        return autoMapper
            .toDomain(entityGuardada);
    }

    @Override
    public Optional<Auto> buscarPorId(Long autoId) {
        return autoJpaRepository
            .findById(autoId)
            .map(autoMapper::toDomain);
    }

    @Override
    public List<Auto> buscarPorPlaca(String placa) {
        return autoJpaRepository
            .findByPlacaIgnoreCase(placa)
            .stream()
            .map(autoMapper::toDomain)
            .toList();
    }

    @Override
    public List<Auto> buscarDisponibles() {
        return autoJpaRepository
            .findByEstado(EstadosNegocio.AUTO_DISPONIBLE)
            .stream()
            .map(autoMapper::toDomain)
            .toList();
    }

    public List<Auto> listarTodos() {
        return autoJpaRepository
            .findAll()
            .stream()
            .map(autoMapper::toDomain)
            .toList();
    }

}
