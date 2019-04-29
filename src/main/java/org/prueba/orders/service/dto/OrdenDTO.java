package org.prueba.orders.service.dto;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

/**
 * A DTO for the Orden entity.
 */
public class OrdenDTO implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

    private BigDecimal total;

    private List<RegistroDTO> registros;
    
    public List<RegistroDTO> getRegistros() {
		return registros;
	}

	public void setRegistros(List<RegistroDTO> registros) {
		this.registros = registros;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrdenDTO ordenDTO = (OrdenDTO) o;
        if (ordenDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), ordenDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "OrdenDTO{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            "}";
    }
}
