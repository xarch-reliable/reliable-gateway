package org.xarch.reliable.service.gateway.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.xarch.reliable.service.AnalysisJWT;

/**
 * 自定义Gateway Filter Factory
 * 
 * @author Wei
 *
 */
@Component
public class AuthorizeGatewayFilterFactory extends AbstractGatewayFilterFactory<AuthorizeGatewayFilterFactory.Config> {

	
	@Autowired
	private AnalysisJWT analysisJWT;

	public AuthorizeGatewayFilterFactory() {
		super(Config.class);
	}

	@Override
	public GatewayFilter apply(Config config) {
		// TODO Auto-generated method stub
		return (exchange, chain) -> {
			String openid = analysisJWT.getToken(exchange);
			if (openid == null) {
				exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
				return exchange.getResponse().setComplete();
			}
			return chain.filter(exchange);
		};
	}

	public static class Config {
		// 控制是否开启认证
		private boolean enabled;

		public boolean isEnabled() {
			return enabled;
		}

		public void setEnabled(boolean enabled) {
			this.enabled = enabled;
		}
	}
}
