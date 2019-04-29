package org.prueba.orders.domain;



import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Moneda.
 */
@Entity
@Table(name = "moneda")
public class Moneda implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Size(min = 3, max = 3)
    @Column(name = "simbolo", length = 3, nullable = false, unique = true)
    private String simbolo;

    @NotNull
    @Size(min = 3, max = 50)
    @Column(name = "descripcion", length = 50, nullable = false)
    private String descripcion;
    
    @Column(name = "por_defecto")
    private Boolean porDefecto=Boolean.FALSE;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public boolean isPorDefecto() {
		return porDefecto;
	}

	public void setPorDefecto(boolean porDefecto) {
		this.porDefecto = porDefecto;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Moneda moneda = (Moneda) o;
        if (moneda.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), moneda.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Moneda{" +
            "id =" + getId() +
            ", simbolo='" + getSimbolo() + "'" +
            ", descripcion='" + getDescripcion() + "'" +
            "}";
    }
    
    
}
