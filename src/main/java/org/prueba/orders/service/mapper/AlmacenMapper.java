package org.prueba.orders.service.mapper;

import org.prueba.orders.domain.Almacen;
import org.prueba.orders.domain.Producto;
import org.prueba.orders.service.dto.AlmacenDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity Almacen and its DTO AlmacenDTO.
 */
@Component
public class AlmacenMapper {

	private ProductoMapper almacenMapper;
	
	public AlmacenMapper(ProductoMapper almacenMapper) {
		super();
		this.almacenMapper=almacenMapper;
	}
	public AlmacenDTO convertAlmacen(Almacen prd){
    	AlmacenDTO pro =new AlmacenDTO();
    	pro.setCantidad(prd.getCantidad());
    	pro.setId(prd.getId());
    	pro.setProducto(this.almacenMapper.convertProducto(prd.getProducto()));
    	return pro;
    }
	public Almacen convertAlmacenDTO(AlmacenDTO prd){
    	Almacen almacen =new Almacen();
    	almacen.setId(prd.getId());
    	almacen.setCantidad(prd.getCantidad());
    	almacen.setProducto(new Producto());
    	almacen.getProducto().setId(prd.getProducto().getId());
    	return almacen;
    }
	
	
}
