package org.prueba.orders.repository;

import org.prueba.orders.domain.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Producto entity.
 */
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {

}
