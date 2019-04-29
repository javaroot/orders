package org.prueba.orders.domain;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;


public class UltimoValor implements Serializable{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private boolean success;
	private Long timestamp;
	private String base;
	private Date date;
	private Map<String, BigDecimal> rates;
	
	public UltimoValor() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UltimoValor(boolean success, Long timestamp, String base, Date date, Map<String, BigDecimal> rates) {
		super();
		this.success = success;
		this.timestamp = timestamp;
		this.base = base;
		this.date = date;
		this.rates = rates;
	}
	
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public Long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}
	public String getBase() {
		return base;
	}
	public void setBase(String base) {
		this.base = base;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Map<String, BigDecimal> getRates() {
		return rates;
	}
	public void setRates(Map<String, BigDecimal> rates) {
		this.rates = rates;
	}	
	
	
}
