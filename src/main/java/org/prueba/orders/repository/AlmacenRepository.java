package org.prueba.orders.repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.prueba.orders.domain.Almacen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Moneda entity.
 */
@Repository
public interface AlmacenRepository extends JpaRepository<Almacen, Long> {
	
	List<Almacen> findByProductoIdIn(Set<Long> ids);
	
	Optional<Almacen> findByProductoId(Long id);
	
}
