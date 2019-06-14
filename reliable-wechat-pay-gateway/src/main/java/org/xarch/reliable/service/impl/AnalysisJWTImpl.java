package org.xarch.reliable.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.xarch.reliable.config.weixin.WxConfig;
import org.xarch.reliable.service.AnalysisJWT;
import org.xarch.reliable.utils.jwt.JwtUtils;

import io.jsonwebtoken.Claims;

@Service
public class AnalysisJWTImpl implements AnalysisJWT {

	private static final Logger logger = LoggerFactory.getLogger(AnalysisJWTImpl.class);
	
	private static final String AUTHORIZE_TOKEN = "Authorization";
	
	@Autowired
	private WxConfig wxConfig;

	@Override
	public String getToken(ServerWebExchange exchange) {
		ServerHttpRequest request = exchange.getRequest();
		HttpHeaders headers = request.getHeaders();
		String token = headers.getFirst(AUTHORIZE_TOKEN);
		if (token == null) {
			token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
		}
		if (token == null) {
			return null;
		}
		// TODO JWT -> getToken
		try {
			logger.info("[token]"+token);
			logger.info("[jwt]"+wxConfig.getJwtKey());
			Claims claims = JwtUtils.parseJWT(wxConfig.getJwtKey(),token);
			return (String) claims.get("openid");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
