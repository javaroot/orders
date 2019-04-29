package org.prueba.orders.service;

import org.prueba.orders.service.dto.OrdenDTO;
import org.prueba.orders.service.dto.RegistroDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Orden.
 */
public interface OrdenService {

    /**
     * Save a orden.
     *
     * @param ordenDTO the entity to save
     * @return the persisted entity
     */
    OrdenDTO save(OrdenDTO ordenDTO);

    /**
     * Get all the ordens.
     *
     * @return the list of entities
     */
    List<OrdenDTO> findAll();


    /**
     * Get the "id" orden.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<OrdenDTO> findOne(Long id);

    /**
     * Delete the "id" orden.
     *
     * @param id the id of the entity
     */
    void delete(Long id);

    /**
     * 
     * @param id
     * @param moneda
     * @return
     */
	OrdenDTO findOneMoneda(Long id, String moneda);

	OrdenDTO deleteRegistroToOrden(Long id, Long idRegistro);

	
	OrdenDTO addRegistroToOrden(Long id, RegistroDTO registro);

	OrdenDTO update(OrdenDTO ordenDTO);
}
