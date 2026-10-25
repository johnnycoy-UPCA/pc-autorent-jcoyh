package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.repository;

import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity.ClienteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteJpaRepository extends JpaRepository<ClienteEntity, Long> {
    Optional<ClienteEntity> findByDocumento(Long documento);
}
