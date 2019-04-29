package org.prueba.orders.repository;

import java.util.Optional;

import org.prueba.orders.domain.Moneda;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Moneda entity.
 */
@Repository
public interface MonedaRepository extends JpaRepository<Moneda, Long> {
	
	Optional<Moneda> findBySimbolo(String simbolo);
	
	Optional<Moneda> findByPorDefecto(boolean porDefecto);
	
}
