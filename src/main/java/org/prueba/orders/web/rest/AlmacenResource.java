package org.prueba.orders.web.rest;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.prueba.orders.domain.Almacen;
import org.prueba.orders.repository.AlmacenRepository;
import org.prueba.orders.repository.ProductoRepository;
import org.prueba.orders.service.dto.AlmacenDTO;
import org.prueba.orders.service.mapper.AlmacenMapper;
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
 * REST controller for managing Almacen.
 */
@RestController
@RequestMapping("/api")
@Api(value="CRUD de Almacenes")
public class AlmacenResource {

    private final Logger log = LoggerFactory.getLogger(AlmacenResource.class);


    private final AlmacenRepository almacenRepository;
    private final AlmacenMapper almacenMapper;
    private final ProductoRepository productoRepository;

    public AlmacenResource(AlmacenRepository almacenRepository,ProductoRepository productoRepository, AlmacenMapper almacenMapper) {
        super();
    	this.almacenRepository = almacenRepository;
        this.almacenMapper=almacenMapper;
        this.productoRepository=productoRepository;
    }

    /**
     * POST  /almacenes : Create a new almacen.
     *
     * @param almacen the almacen to create
     * @return the ResponseEntity with status 201 (Created) and with body the new almacen, or with status 400 (Bad Request) if the almacen has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Crear una almacen")
    @PostMapping("/almacenes")
    @ResponseStatus(HttpStatus.CREATED)
    public AlmacenDTO createAlmacen(@Valid @RequestBody AlmacenDTO almacenDto) throws URISyntaxException {
        log.debug("REST request to save Almacen : {}", almacenDto);
        almacenDto.setId(null);
        if (almacenDto.getProducto().getId()==null) {
        	throw new  OrdenServiceValidationException("Es necesario el id del producto para crear el Almacen");
        }
        Optional<Almacen> opAlm=this.almacenRepository.findByProductoId(almacenDto.getProducto().getId());
		if(opAlm.isPresent()){throw new OrdenServiceValidationException("El producto ya esta asociado a otro Almacen");}
        Almacen almacen=almacenMapper.convertAlmacenDTO(almacenDto);
        almacen.setProducto(this.productoRepository.findById(almacen.getProducto().getId()).
        		orElseThrow(()-> new OrdenNotFoundException("Producto no encontrado")));
        
        return almacenMapper.convertAlmacen(almacenRepository.save(almacen));
    }

    /**
     * PUT  /almacenes : Updates an existing almacen.
     *
     * @param almacen the almacen to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated almacen,
     * or with status 400 (Bad Request) if the almacen is not valid,
     * or with status 500 (Internal Server Error) if the almacen couldn't be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @ApiOperation(value = "Actualizar una almacen")
    @PutMapping("/almacenes")
    @ResponseStatus(HttpStatus.OK)
    public Almacen updateAlmacen(@Valid @RequestBody AlmacenDTO almacenDTO) throws URISyntaxException {
        log.debug("REST request to update Almacen : {}", almacenDTO);
        if (almacenDTO.getId() == null) {
        	throw new OrdenServiceValidationException("Es necesario el id de la Almacen para modificar");
        }
        
        Almacen almacenBD =almacenRepository.findById(almacenDTO.getId()).orElseThrow(()-> new OrdenNotFoundException("Almacen no encontrada"));
        Almacen almacen=almacenMapper.convertAlmacenDTO(almacenDTO);
        if(almacenBD.getProducto().getId()!=almacenDTO.getProducto().getId()) {
        	Optional<Almacen> opAlm=this.almacenRepository.findByProductoId(almacenDTO.getProducto().getId());
    		if(opAlm.isPresent()){throw new OrdenServiceValidationException("El producto ya esta asociado a otro Almacen");}
            almacen.setProducto(this.productoRepository.findById(almacen.getProducto().getId()).
        		orElseThrow(()-> new OrdenNotFoundException("Producto no encontrado")));
        }
        return almacenRepository.save(almacen);
    }

    /**
     * GET  /almacenes : get all the almacenes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of almacenes in body
     */
    @ApiOperation(value = "Obtener la lista de almacenes", response = List.class)
    @GetMapping("/almacenes")
    @ResponseStatus(HttpStatus.OK)
    public List<AlmacenDTO> getAllAlmacens() {
        log.debug("REST request to get all Almacens");
        return almacenRepository.findAll().stream()
                .map(almacenMapper::convertAlmacen)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * GET  /almacenes/:id : get the "id" almacen.
     *
     * @param id the id of the almacen to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the almacen, or with status 404 (Not Found)
     */
    @ApiOperation(value = "Obtener una almacen")
    @GetMapping("/almacenes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AlmacenDTO getAlmacen(@PathVariable Long id) {
        log.debug("REST request to get Almacen : {}", id);
        return almacenRepository.findById(id).map(almacenMapper::convertAlmacen).orElseThrow(()-> new OrdenNotFoundException("La almacen no existe"));
    }

    /**
     * DELETE  /almacenes/:id : delete the "id" almacen.
     *
     * @param id the id of the almacen to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @ApiOperation(value = "Eliminar una almacen")
    @DeleteMapping("/almacenes/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAlmacen(@PathVariable Long id) {
        log.debug("REST request to delete Almacen : {}", id);
        almacenRepository.deleteById(id);
    }
}
