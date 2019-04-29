package org.prueba.orders.domain;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;

/**
 * A Registro.
 */
@Entity
@Table(name = "registro")
public class Registro implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Min(value = 1)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "sub_total", precision = 10, scale = 2)
    private BigDecimal subTotal;

    @ManyToOne(optional = false,fetch=FetchType.EAGER)
    @JoinColumn(name = "PRODUCTO_ID", nullable = false)
    private Producto producto;
    
    @ManyToOne
    @JoinColumn(name = "ORDEN_ID", nullable = false, updatable=false)
    private Orden orden;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public Registro cantidad(Integer cantidad) {
        this.cantidad = cantidad;
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public BigDecimal getSubTotal() {
        return subTotal;
    }

    public Registro subTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
        return this;
    }

    public void setSubTotal(BigDecimal subTotal) {
        this.subTotal = subTotal;
    }

    public Producto getProducto() {
        return producto;
    }

    public Registro producto(Producto producto) {
        this.producto = producto;
        return this;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    public Orden getOrden() {
        return orden;
    }

    public Registro orden(Orden orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Orden orden) {
        this.orden = orden;
    }
    
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Registro registro = (Registro) o;
        if (registro.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), registro.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Registro{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", subTotal=" + getSubTotal() +
            ", producto=" +this.producto!=null?this.producto.toString():"nulo" +
            "}";
    }
}
