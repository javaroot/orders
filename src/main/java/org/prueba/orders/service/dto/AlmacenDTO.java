package org.prueba.orders.service.dto;
import java.io.Serializable;
import java.util.Objects;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * A DTO for the Registro entity.
 */
public class AlmacenDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    @NotNull
    @Min(value = 1)
    private Integer cantidad;

    private ProductoDTO producto;
    
    public ProductoDTO getProducto() {
		return producto;
	}

	public void setProducto(ProductoDTO productoDTO) {
		this.producto = productoDTO;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AlmacenDTO registroDTO = (AlmacenDTO) o;
        if (registroDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registroDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AlmacenDTO{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", producto=" + this.producto==null?"nulo":this.producto.toString()+
            "}";
    }
}
