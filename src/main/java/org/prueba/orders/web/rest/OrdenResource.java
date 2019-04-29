package org.prueba.orders.web.rest;
import java.net.URISyntaxException;
import java.util.List;

import org.prueba.orders.service.OrdenService;
import org.prueba.orders.service.dto.OrdenDTO;
import org.prueba.orders.service.dto.RegistroDTO;
import org.prueba.orders.web.rest.util.OrdenNotFoundException;
import org.prueba.orders.web.rest.util.OrdenServiceValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * REST controller for managing Orden.
 */
@RestController
@RequestMapping("/api")
@Api(value="Manejador de Ordenes")
public class OrdenResource {

    private final Logger log = LoggerFactory.getLogger(OrdenResource.class);

    private final OrdenService ordenService;

    public OrdenResource(OrdenService ordenService) {
        this.ordenService = ordenService;
    }

    /**
     * POST  /ordens : Create a new orden.
     *
     * @param ordenDTO the ordenDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new ordenDTO, or with status 400 (Bad Request) if the orden has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Crear una orden")
    @PostMapping("/ordenes")
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenDTO createOrden(@RequestBody OrdenDTO ordenDTO) throws URISyntaxException {
        log.debug("REST request to save Orden : {}", ordenDTO);
        ordenDTO.setId(null);
        return ordenService.save(ordenDTO);
    }
    
    /**
     * POST  /ordenes/id/productos : Add a new product To Orden.
     *
     * @param ordenDTO the ordenDTO to create
     * @return ordenDTO and status 201 created
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Agregar un producto a una orden")
    @PostMapping("/ordenes/{id}/registros")
    @ResponseStatus(HttpStatus.CREATED)
    public OrdenDTO addProductoToOrden(@PathVariable Long id,@RequestBody RegistroDTO registro) throws URISyntaxException {
        log.debug("REST request to add producto : {}", registro);
        registro.setId(null);
        return ordenService.addRegistroToOrden(id,registro);
    }

    /**
     * PUT  /ordens : Updates an existing orden.
     *
     * @param ordenDTO the ordenDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated ordenDTO,
     * or with status 400 (Bad Request) if the ordenDTO is not valid,
     * or with status 500 (Internal Server Error) if the ordenDTO couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Actualizar una orden")
    @PutMapping("/ordenes")
    @ResponseStatus(HttpStatus.OK)
    public OrdenDTO updateOrden(@RequestBody OrdenDTO ordenDTO) throws URISyntaxException {
        log.debug("REST request to update Orden : {}", ordenDTO);
        if (ordenDTO.getId()==null) {
        	throw new  OrdenServiceValidationException("Es necesario el id de la Orden para modificar");
        }
        return ordenService.update(ordenDTO);
    }

    /**
     * GET  /ordens : get all the ordens.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of ordens in body
     */
    @GetMapping("/ordenes")
    @ApiOperation(value = "Obtener lista de ordenes", response = List.class)
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<OrdenDTO>> getAllOrdens() {
        log.debug("REST request to get all Ordens");
        return ResponseEntity.ok(ordenService.findAll());
    }

    /**
     * GET  /ordens/:id : get the "id" orden.
     *
     * @param id the id of the ordenDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the ordenDTO, or with status 404 (Not Found)
     */
    @ApiOperation(value = "Obtener una orden")
    @GetMapping("/ordenes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrdenDTO getOrden(@PathVariable Long id,@RequestParam(defaultValue = "") String moneda) {
        log.debug("REST request to get Orden : {}", id);
        if(moneda.isEmpty()) {
        	return ordenService.findOne(id).orElseThrow(()->new OrdenNotFoundException("Orden no encontrada"));
        }else {
        	return ordenService.findOneMoneda(id, moneda);
        }
    }

    /**
     * DELETE  /ordens/:id : delete the "id" orden.
     *
     * @param id the id of the ordenDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "Eliminar una orden")
    @DeleteMapping("/ordenes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteOrden(@PathVariable Long id) {
        log.debug("REST request to delete Orden : {}", id);
        ordenService.delete(id);
    }
    
    /**
     * DELETE  /ordens/:id : delete the "id" orden.
     *
     * @param id the id of the ordenDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "Eliminar un producto de una orden")
    @DeleteMapping("/ordenes/{id}/registros/{idRegistro}")
    @ResponseStatus(HttpStatus.OK)
    public OrdenDTO deleteRegistroToOrden(@PathVariable Long id,@PathVariable Long idRegistro) {
        log.debug("REST request to delete Producto : "+idRegistro+" de la orden "+id);
        return ordenService.deleteRegistroToOrden(idRegistro, idRegistro);
    }
}
