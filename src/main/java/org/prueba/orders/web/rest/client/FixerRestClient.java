package org.prueba.orders.web.rest.client;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.prueba.orders.domain.UltimoValor;
import org.prueba.orders.web.rest.util.OrdenServiceException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class FixerRestClient {
	
	private final String API_ACCESS_KEY = "52eafae94accdad9ffa58b143024cdf6";

	private RestTemplate restTemplate;

	public FixerRestClient(RestTemplate restTemplate) {
		
		this.restTemplate = restTemplate;		
	}
	
	
	public UltimoValor latestRate(String base,String symbols) {
		
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://data.fixer.io/api/latest")
				.queryParam("access_key", API_ACCESS_KEY)
				.queryParam("symbols", symbols);
		
		System.out.println("Peticion"+builder.toUriString());
		UltimoValor uv =restTemplate.getForObject(builder.toUriString(), UltimoValor.class);
		if(uv==null){
			throw new OrdenServiceException("Erro en el servicio de Cosulta de divisas Fixer");
		}
		BigDecimal valBase=uv.getRates().get(base);
		uv.getRates().entrySet().forEach(x-> x.setValue(valBase.divide(x.getValue(),4,RoundingMode.HALF_UP)));
		return uv;
	}
	
}
