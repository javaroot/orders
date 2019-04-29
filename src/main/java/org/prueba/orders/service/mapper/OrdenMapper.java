package org.prueba.orders.service.mapper;

import java.util.stream.Collectors;

import org.prueba.orders.domain.Orden;
import org.prueba.orders.domain.Producto;
import org.prueba.orders.domain.Registro;
import org.prueba.orders.service.dto.OrdenDTO;
import org.prueba.orders.service.dto.RegistroDTO;
import org.springframework.stereotype.Component;
@Component
public class OrdenMapper {

	private ProductoMapper productoMapper;
	
	public OrdenMapper(ProductoMapper productoMapper) {
		this.productoMapper=productoMapper;
	}

	public OrdenDTO convertOrden(Orden ord){
		OrdenDTO dto=new OrdenDTO();
		dto.setId(ord.getId());
		dto.setTotal(ord.getTotal());
		dto.setRegistros(ord.getRegistros().stream().map(tmp-> {
			RegistroDTO reg=new RegistroDTO();
			reg.setId(tmp.getId());
			reg.setCantidad(tmp.getCantidad());
			reg.setProducto(this.productoMapper.convertProducto(tmp.getProducto()));
			return reg;
		}).collect(Collectors.toList()));
		return dto;
    }
	
	public Orden convertOrdenDTO(OrdenDTO ord){
		Orden orden=new Orden();
		orden.setId(ord.getId());
		orden.setTotal(ord.getTotal());
		orden.setRegistros(ord.getRegistros().stream().map(tmp-> {
			Registro reg=new Registro();
			reg.setId(tmp.getId());
			reg.setCantidad(tmp.getCantidad());
			reg.setProducto(this.productoMapper.convertProductoDTO(tmp.getProducto()));
			reg.setOrden(orden);
			return reg;
		}).collect(Collectors.toList()));
    	return orden;
    }
	
	public Registro convertRegistroDTO(RegistroDTO reg){
    	Registro registro =new Registro();
    	registro.setCantidad(reg.getCantidad());
    	registro.setProducto(new Producto());
    	registro.getProducto().setId(reg.getProducto().getId());
    	return registro;
    }
}
