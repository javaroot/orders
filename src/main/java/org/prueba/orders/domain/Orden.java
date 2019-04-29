package org.prueba.orders.domain;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * A Orden.
 */
@Entity
@Table(name = "orden")
public class Orden implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "total", precision = 10, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "orden", cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    private List<Registro> registros = new ArrayList<>();

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

    public List<Registro> getRegistros() {
        return registros;
    }

    public Orden addRegistro(Registro registro) {
        this.registros.add(registro);
        registro.setOrden(this);
        return this;
    }

    public Orden removeRegistro(Registro registro) {
        this.registros.remove(registro);
        registro.setOrden(null);
        return this;
    }

    public void setRegistros(List<Registro> registros) {
        this.registros = registros;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Orden orden = (Orden) o;
        if (orden.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), orden.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Orden{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            "}";
    }
}
