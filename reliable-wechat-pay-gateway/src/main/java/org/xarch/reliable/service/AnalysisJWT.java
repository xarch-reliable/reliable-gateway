package org.xarch.reliable.service;

import org.springframework.web.server.ServerWebExchange;

public interface AnalysisJWT {

	/**
	 * headers.Authorization[token] -> openid [null]
	 */
	public String getToken(ServerWebExchange exchange);
	
}
