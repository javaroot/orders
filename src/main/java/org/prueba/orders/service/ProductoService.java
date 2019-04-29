package org.prueba.orders.service;

import org.prueba.orders.service.dto.ProductoDTO;
import org.prueba.orders.web.rest.util.OrdenNotFoundException;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing Producto.
 */
public interface ProductoService {

    /**
     * Save a producto.
     *
     * @param productoDTO the entity to save
     * @return the persisted entity
     * @throws OrdenNotFoundException 
     */
    ProductoDTO save(ProductoDTO productoDTO) throws OrdenNotFoundException;

    /**
     * Get all the productos.
     *
     * @return the list of entities
     */
    List<ProductoDTO> findAll();


    /**
     * Get the "id" producto.
     *
     * @param id the id of the entity
     * @return the entity
     */
    Optional<ProductoDTO> findOne(Long id);

    /**
     * Delete the "id" producto.
     *
     * @param id the id of the entity
     */
    void delete(Long id);
}
