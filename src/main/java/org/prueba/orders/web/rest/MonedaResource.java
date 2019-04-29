package org.prueba.orders.web.rest;
import java.net.URISyntaxException;
import java.util.List;

import javax.validation.Valid;

import org.prueba.orders.domain.Moneda;
import org.prueba.orders.repository.MonedaRepository;
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
 * REST controller for managing Moneda.
 */
@RestController
@RequestMapping("/api")
@Api(value="CRUD de Monedas")
public class MonedaResource {

    private final Logger log = LoggerFactory.getLogger(MonedaResource.class);


    private final MonedaRepository monedaRepository;

    public MonedaResource(MonedaRepository monedaRepository) {
        this.monedaRepository = monedaRepository;
    }

    /**
     * POST  /monedas : Create a new moneda.
     *
     * @param moneda the moneda to create
     * @return the ResponseEntity with status 201 (Created) and with body the new moneda, or with status 400 (Bad Request) if the moneda has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Crear una moneda")
    @PostMapping("/monedas")
    @ResponseStatus(HttpStatus.CREATED)
    public Moneda createMoneda(@Valid @RequestBody Moneda moneda) throws URISyntaxException {
        log.debug("REST request to save Moneda : {}", moneda);
        moneda.setId(null);
        return monedaRepository.save(moneda);
    }

    /**
     * PUT  /monedas : Updates an existing moneda.
     *
     * @param moneda the moneda to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated moneda,
     * or with status 400 (Bad Request) if the moneda is not valid,
     * or with status 500 (Internal Server Error) if the moneda couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Actualizar una moneda")
    @PutMapping("/monedas")
    @ResponseStatus(HttpStatus.OK)
    public Moneda updateMoneda(@Valid @RequestBody Moneda moneda) throws URISyntaxException {
        log.debug("REST request to update Moneda : {}", moneda);
        if (moneda.getId() == null) {
        	throw new OrdenServiceValidationException("Es necesario el id de la Moneda para modificar");
        }
        monedaRepository.findById(moneda.getId()).orElseThrow(()-> new OrdenNotFoundException("Moneda no encontrada"));
        return monedaRepository.save(moneda);
    }

    /**
     * GET  /monedas : get all the monedas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of monedas in body
     */
    @ApiOperation(value = "Obtener la lista de monedas", response = List.class)
    @GetMapping("/monedas")
    @ResponseStatus(HttpStatus.OK)
    public List<Moneda> getAllMonedas() {
        log.debug("REST request to get all Monedas");
        return monedaRepository.findAll();
    }

    /**
     * GET  /monedas/:id : get the "id" moneda.
     *
     * @param id the id of the moneda to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the moneda, or with status 404 (Not Found)
     */
    @ApiOperation(value = "Obtener una moneda")
    @GetMapping("/monedas/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Moneda getMoneda(@PathVariable Long id) {
        log.debug("REST request to get Moneda : {}", id);
        return monedaRepository.findById(id).orElseThrow(()-> new OrdenNotFoundException("La moneda no existe"));
    }

    /**
     * DELETE  /monedas/:id : delete the "id" moneda.
     *
     * @param id the id of the moneda to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "Eliminar una moneda")
    @DeleteMapping("/monedas/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteMoneda(@PathVariable Long id) {
        log.debug("REST request to delete Moneda : {}", id);
        monedaRepository.deleteById(id);
    }
}
