package org.prueba.orders.web.rest;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.prueba.orders.service.ProductoService;
import org.prueba.orders.service.dto.ProductoDTO;
import org.prueba.orders.web.rest.util.OrdenNotFoundException;
import org.prueba.orders.web.rest.util.OrdenServiceValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST controller para CRUD de Productos.
 */
@RestController
@RequestMapping("/api")
@Api(value="CRUD de Productos")
public class ProductoResource{

    private final Logger log = LoggerFactory.getLogger(ProductoResource.class);

    private static final String ENTITY_NAME = "producto";

    private final ProductoService productoService;

    public ProductoResource(ProductoService productoService) {
        this.productoService = productoService;
    }

    /**
     * POST  /productos : Create a new producto.
     *
     * @param productoDTO the productoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new productoDTO, or with status 400 (Bad Request) if the producto has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Crear un producto")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/productos")
    public ProductoDTO createProducto(@Valid @RequestBody ProductoDTO productoDTO) throws URISyntaxException {
        log.debug("REST request to save Producto : {}", productoDTO);
        productoDTO.setId(null);
        ProductoDTO result = productoService.save(productoDTO);
        return result;
    }

    /**
     * PUT  /productos : Updates an existing producto.
     *
     * @param productoDTO the productoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated productoDTO,
     * or with status 400 (Bad Request) if the productoDTO is not valid,
     * or with status 500 (Internal Server Error) if the productoDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     * @throws OrdenNotFoundException 
     */
    @ApiOperation(value = "Actualizar un producto")
    @PutMapping("/productos")
    @ResponseStatus(HttpStatus.OK)
    public ProductoDTO updateProducto(@Valid @RequestBody ProductoDTO productoDTO) throws URISyntaxException {
        log.debug("REST request to update Producto : {}", productoDTO);
        if (productoDTO.getId() == null) {
        	throw new OrdenServiceValidationException("Es necesario el Id del "+ENTITY_NAME);
        }
        productoService.findOne(productoDTO.getId()).orElseThrow(()-> new OrdenNotFoundException(ENTITY_NAME+" no encontrado"));
        ProductoDTO result = productoService.save(productoDTO);
        return result;
    }

    /**
     * GET  /productos : get all the productos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of productos in body
     */
    @ApiOperation(value = "Obtener la lista de productos", response = List.class)
    @GetMapping("/productos")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductoDTO> getAllProductos() {
        log.debug("REST request to get all Productos");
        return productoService.findAll();
    }

    /**
     * GET  /productos/:id : get the "id" producto.
     *
     * @param id the id of the productoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the productoDTO, or with status 404 (Not Found)
     * @throws OrdenNotFoundException 
     */
    @ApiOperation(value = "Obtener un producto")
    @GetMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ProductoDTO getProducto(@PathVariable Long id){
        log.debug("REST request to get Producto : {}", id);
        return productoService.findOne(id).orElseThrow(() -> new OrdenNotFoundException("El producto no existe"));
    }

    /**
     * DELETE  /productos/:id : delete the "id" producto.
     *
     * @param id the id of the productoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "Eliminar un producto")
    @DeleteMapping("/productos/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteProducto(@PathVariable Long id) {
        log.debug("REST request to delete Producto : {}", id);
        productoService.delete(id);
    }
}
