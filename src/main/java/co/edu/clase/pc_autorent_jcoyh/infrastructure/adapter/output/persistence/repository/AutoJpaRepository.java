package co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.repository;

import co.edu.clase.pc_autorent_jcoyh.infrastructure.adapter.output.persistence.entity.AutoEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AutoJpaRepository extends JpaRepository<AutoEntity, Long> {
    List<AutoEntity> findByPlacaIgnoreCase(String placa);
    List<AutoEntity> findByEstado(String estado);
}
