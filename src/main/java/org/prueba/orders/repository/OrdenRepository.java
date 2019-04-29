package org.prueba.orders.repository;

import org.prueba.orders.domain.Orden;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Orden entity.
 */
@Repository
public interface OrdenRepository extends JpaRepository<Orden, Long> {

}
