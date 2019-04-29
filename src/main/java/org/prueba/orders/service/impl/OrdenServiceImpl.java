package org.prueba.orders.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.prueba.orders.domain.Almacen;
import org.prueba.orders.domain.Moneda;
import org.prueba.orders.domain.Orden;
import org.prueba.orders.domain.Producto;
import org.prueba.orders.domain.Registro;
import org.prueba.orders.domain.UltimoValor;
import org.prueba.orders.repository.AlmacenRepository;
import org.prueba.orders.repository.MonedaRepository;
import org.prueba.orders.repository.OrdenRepository;
import org.prueba.orders.repository.ProductoRepository;
import org.prueba.orders.service.OrdenService;
import org.prueba.orders.service.dto.OrdenDTO;
import org.prueba.orders.service.dto.RegistroDTO;
import org.prueba.orders.service.mapper.OrdenMapper;
import org.prueba.orders.web.rest.client.FixerRestClient;
import org.prueba.orders.web.rest.util.OrdenNotFoundException;
import org.prueba.orders.web.rest.util.OrdenServiceValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing Orden.
 */
@Service
@Transactional
public class OrdenServiceImpl implements OrdenService {

    private final Logger log = LoggerFactory.getLogger(OrdenServiceImpl.class);

    private final OrdenRepository ordenRepository;

    private final OrdenMapper ordenMapper;
    
    private final ProductoRepository productoRepository;
    
    private final MonedaRepository monedaRepository;
    
    private final FixerRestClient fixerRestClient;
    
    private final AlmacenRepository almacenRepository;
    

    public OrdenServiceImpl(OrdenRepository ordenRepository, OrdenMapper ordenMapper, 
    		ProductoRepository productoRepository,MonedaRepository monedaRepository,
    		AlmacenRepository almacenRepository,FixerRestClient fixerRestClient) {
        this.ordenRepository = ordenRepository;
        this.ordenMapper = ordenMapper;
        this.productoRepository=productoRepository;
        this.monedaRepository=monedaRepository;
        this.fixerRestClient=fixerRestClient;
        this.almacenRepository=almacenRepository;
    }

    /**
     * Save a orden.
     *
     * @param ordenDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrdenDTO save(OrdenDTO ordenDTO) {
        log.debug("Request to save Orden : {}", ordenDTO);
        Orden orden = ordenMapper.convertOrdenDTO(ordenDTO);
        Moneda monOrden= monedaRepository.findByPorDefecto(true).orElseThrow(()->new OrdenNotFoundException("No existe una moneda por defecto"));
        orden.getRegistros().forEach(tmp -> {
        			tmp.setProducto(this.productoRepository.findById(tmp.getProducto().getId()).
        			orElseThrow(()->new OrdenNotFoundException("producto con el sku "+tmp.getProducto().getSku()+" no econtrado")));
        });
        orden.setTotal(calculaTotal(orden, monOrden.getSimbolo()));
        actualizarAlmacen(orden);
        orden = ordenRepository.save(orden);
        return ordenMapper.convertOrden(orden);
    }

    /**
     * Update a orden.
     *
     * @param ordenDTO the entity to save
     * @return the persisted entity
     */
    @Override
    public OrdenDTO update(OrdenDTO ordenDTO) {
        log.debug("Request to update Orden : {}", ordenDTO);
        Orden orden=ordenRepository.findById(ordenDTO.getId()).orElseThrow(()->new OrdenNotFoundException("Orden no encontrada"));
        Moneda monOrden= monedaRepository.findByPorDefecto(true).orElseThrow(()->new OrdenNotFoundException("No existe una moneda por defecto"));
        List<Long> ids=orden.getRegistros().stream().map(Registro::getId).collect(Collectors.toList());
        orden.setRegistros(ordenDTO.getRegistros().stream().filter(dto->ids.contains(dto.getId())).map(ordenMapper::convertRegistroDTO).collect(Collectors.toList()));
        orden.setTotal(calculaTotal(orden, monOrden.getSimbolo()));
        actualizarAlmacen(orden);
        orden = ordenRepository.save(orden);
        return ordenMapper.convertOrden(orden);
    }
    /**
     * Get all the ordens.
     *
     * @return the list of entities
     */
    @Override
    @Transactional(readOnly = true)
    public List<OrdenDTO> findAll() {
        log.debug("Request to get all Ordens");
        return ordenRepository.findAll().stream()
            .map(ordenMapper::convertOrden)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one orden by id.
     *
     * @param id the id of the entity
     * @return the entity
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<OrdenDTO> findOne(Long id) {
        log.debug("Request to get Orden : {}", id);
        return ordenRepository.findById(id)
            .map(ordenMapper::convertOrden);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OrdenDTO findOneMoneda(Long id,String moneda) {
        log.debug("Request to get Orden : {}", id);
        Orden ord =ordenRepository.findById(id).orElseThrow(()-> new OrdenNotFoundException("orden no encontrada"));
        ord.setTotal(calculaTotal(ord, moneda));
        return ordenMapper.convertOrden(ord);
    }
    
    /**
     *  metodo que calcula el total de una orden de acuerdo a la moneda pasada como parametro
     * @param ord
     * @param moneda
     * @return
     */
    private BigDecimal calculaTotal(Orden ord,String moneda) {
    	Set<String> monendas =ord.getRegistros().stream().map(x -> x.getProducto().getMoneda().getSimbolo()).collect(Collectors.toSet());
        monendas.add(moneda);
        UltimoValor valorMonedas=fixerRestClient.latestRate(moneda,String.join(",", monendas));
        Function<Registro, BigDecimal> subTotalMapper = invoice -> invoice.getProducto().getPrecio().multiply(valorMonedas.getRates().get(invoice.getProducto().getMoneda().getSimbolo())).multiply(BigDecimal.valueOf(invoice.getCantidad()));
        return ord.getRegistros().stream().map(subTotalMapper).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    private void actualizarAlmacen (Orden ord) {
    	Map<Long,Integer> ids=ord.getRegistros().stream().collect(Collectors.toMap(x-> x.getProducto().getId(),y-> y.getCantidad()));
    	List<Almacen> almacenes=this.almacenRepository.findByProductoIdIn(ids.keySet());
		almacenes.forEach(x->{
			x.setCantidad(x.getCantidad()-ids.get(x.getProducto().getId()));
			if(x.getCantidad()<0) {
				throw new OrdenServiceValidationException("No existe suficiente producto "+x.getProducto().getNombre()+" en el Almacen ");
			}
		});
		this.almacenRepository.saveAll(almacenes);
    }
    
    private void agregarAlmacen (Registro reg) {
    	Almacen almacen=this.almacenRepository.findByProductoId(
    			reg.getProducto().getId()).orElseThrow(()->new OrdenNotFoundException("No existe el producto "+reg.getProducto().getNombre()+" en el Almacen"));
		almacen.setCantidad(almacen.getCantidad()+reg.getCantidad());
		this.almacenRepository.save(almacen);
    }
    
    private void quitarAlmacen (Registro reg) {
    	Almacen almacen=this.almacenRepository.findByProductoId(
    			reg.getProducto().getId()).orElseThrow(()->new OrdenNotFoundException("No existe el producto "+reg.getProducto().getNombre()+" en el Almacen"));
		almacen.setCantidad(almacen.getCantidad()-reg.getCantidad());
		if(almacen.getCantidad()<0) {
				throw new OrdenServiceValidationException("No existe suficiente producto "+reg.getProducto().getNombre()+" en el Almacen ");
		}
		this.almacenRepository.save(almacen);
    }
    /**
     * Delete the orden by id.
     *
     * @param id the id of the entity
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Orden : {}", id);
        ordenRepository.deleteById(id);
    }

	@Override
	public OrdenDTO deleteRegistroToOrden(Long id, Long idRegistro) {
		Orden ord =ordenRepository.findById(id).orElseThrow(()-> new OrdenNotFoundException("orden no encontrada"));
        Registro reg=ord.getRegistros().stream().filter(x-> x.getId()==idRegistro).findAny().orElseThrow(()->new OrdenNotFoundException("Registro no encontrado en la Orden"));
        ord.getRegistros().remove(reg);
        Moneda monOrden= monedaRepository.findByPorDefecto(true).orElseThrow(()->new OrdenNotFoundException("No existe una moneda por defecto"));
        ord.setTotal(calculaTotal(ord, monOrden.getSimbolo()));
        agregarAlmacen(reg);
        ordenRepository.save(ord);
        return ordenMapper.convertOrden(ord);
	}

	@Override
	public OrdenDTO addRegistroToOrden(Long id, RegistroDTO registro) {
		Orden ord =ordenRepository.findById(id).orElseThrow(()-> new OrdenNotFoundException("orden no encontrada"));
		Producto prd=this.productoRepository.findById(registro.getProducto().getId()).
		orElseThrow(()->new OrdenNotFoundException("producto con el sku "+registro.getProducto().getId()+" no econtrado"));
        Registro reg=ordenMapper.convertRegistroDTO(registro);
		reg.setProducto(prd);
		ord.getRegistros().add(reg);
		Moneda monOrden= monedaRepository.findByPorDefecto(true).orElseThrow(()->new OrdenNotFoundException("No existe una moneda por defecto"));
        ord.setTotal(calculaTotal(ord, monOrden.getSimbolo()));
        quitarAlmacen(reg);
        ordenRepository.save(ord);
		return ordenMapper.convertOrden(ord);
	}
}
