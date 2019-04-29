package org.prueba.orders.repository;

import org.prueba.orders.domain.Registro;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Registro entity.
 */
@Repository
public interface RegistroRepository extends JpaRepository<Registro, Long> {

}
