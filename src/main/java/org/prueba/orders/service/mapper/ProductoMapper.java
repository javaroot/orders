package org.prueba.orders.service.mapper;

import org.prueba.orders.domain.Moneda;
import org.prueba.orders.domain.Producto;
import org.prueba.orders.service.dto.ProductoDTO;
import org.springframework.stereotype.Component;

/**
 * Mapper for the entity Producto and its DTO ProductoDTO.
 */
@Component
public class ProductoMapper {

	public ProductoDTO convertProducto(Producto prd){
    	ProductoDTO pro =new ProductoDTO();
    	pro.setId(prd.getId());
    	pro.setNombre(prd.getNombre());
    	pro.setSku(prd.getSku());
    	pro.setPrecio(prd.getPrecio());
    	pro.setMoneda(prd.getMoneda().getSimbolo());
    	return pro;
    }
	public Producto convertProductoDTO(ProductoDTO prd){
    	Producto producto =new Producto();
    	producto.setId(prd.getId());
    	producto.setNombre(prd.getNombre());
    	producto.setSku(prd.getSku());
    	producto.setPrecio(prd.getPrecio());
    	producto.setMoneda(new Moneda());
    	producto.getMoneda().setSimbolo(prd.getMoneda());
    	return producto;
    }
	
	
}
