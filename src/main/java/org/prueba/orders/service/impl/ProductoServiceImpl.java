package org.prueba.orders.service.impl;

import org.prueba.orders.service.ProductoService;
import org.prueba.orders.domain.Moneda;
import org.prueba.orders.domain.Producto;
import org.prueba.orders.repository.MonedaRepository;
import org.prueba.orders.repository.ProductoRepository;
import org.prueba.orders.service.dto.ProductoDTO;
import org.prueba.orders.service.mapper.ProductoMapper;
import org.prueba.orders.web.rest.util.OrdenNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing Producto.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    private final ProductoMapper productoMapper;
    
    private final MonedaRepository monedaRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository, ProductoMapper productoMapper,MonedaRepository monedaRepository) {
        this.productoRepository = productoRepository;
        this.productoMapper = productoMapper;
        this.monedaRepository=monedaRepository;
    }

    /**
     * Save a producto.
     *
     * @param productoDTO the entity to save
     * @return the persisted entity
     * @throws OrdenNotFoundException 
     */
    @Override
    public ProductoDTO save(ProductoDTO productoDTO) throws OrdenNotFoundException {
        log.debug("Request to save Producto : {}", productoDTO);
        Producto producto = productoMapper.convertProductoDTO(productoDTO);
        Optional<Moneda> moneda=this.monedaRepository.findBySimbolo(producto.getMoneda().getSimbolo());
        producto.setMoneda(moneda.orElseThrow(()-> new OrdenNotFoundException("La moneda no esta soportada actualmente")));
        producto = productoRepository.save(producto);
        return productoMapper.convertProducto(producto);
    }

    /**
     * Get all the productos.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<ProductoDTO> findAll() {
        log.debug("Request to get all Productos");
        return productoRepository.findAll().stream()
            .map(productoMapper::convertProducto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one producto by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ProductoDTO> findOne(Long id) {
        log.debug("Request to get Producto : {}", id);
        return productoRepository.findById(id)
            .map(productoMapper::convertProducto);
    }

    /**
     * Delete the producto by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Producto : {}", id);
        productoRepository.deleteById(id);
    }
}
