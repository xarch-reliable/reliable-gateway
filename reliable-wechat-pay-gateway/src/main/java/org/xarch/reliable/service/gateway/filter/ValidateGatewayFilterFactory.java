package org.xarch.reliable.service.gateway.filter;

import java.util.HashMap;
import java.util.Map;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.BodyInserterContext;
import org.springframework.cloud.gateway.support.CachedBodyOutputMessage;
import org.springframework.cloud.gateway.support.DefaultServerRequest;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.server.ServerWebExchange;
import org.xarch.reliable.service.AnalysisBody;
import org.xarch.reliable.service.AnalysisJWT;

/**
 * 自定义Gateway Filter Factory
 * 
 * @author Wei
 *
 */
@Component
public class ValidateGatewayFilterFactory extends AbstractGatewayFilterFactory<ValidateGatewayFilterFactory.Config> {

	@Autowired
	private AnalysisJWT analysisJWT;

	@Autowired
	private AnalysisBody analysisBody;

	public ValidateGatewayFilterFactory() {
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
			return processRequest(openid, exchange, chain);
		};
	}

	/**
	 * 处理From表单数据
	 */
	private String FromBody(String openid, String body) {
		// origin body map
		Map<String, Object> bodyMap = analysisBody.decodeBody_from(body);
		if (bodyMap != null) {
			// TODO decrypt & auth
			bodyMap.put("openid", openid);
		} else {
			// TODO decrypt & auth
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("openid", openid);
			map.put("error_msg", "From-->body解析错误");
			return analysisBody.encodeBody_from(map);
		}
		return analysisBody.encodeBody_from(bodyMap);
	}

	/**
	 * 处理Json数据
	 */
	private String JsonBody(String openid, String body) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("openid", openid);
		// origin body map
		Map<String, Object> bodyMap = analysisBody.decodeBody_json(body);
		if (bodyMap != null) {
			// TODO decrypt & auth
			map.put("body", bodyMap);
		} else {
			// TODO decrypt & auth
			map.put("error_msg", "Json-->body解析错误");
		}
		return analysisBody.encodeBody_json(map);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private Mono<Void> processRequest(String openid, ServerWebExchange exchange, GatewayFilterChain chain) {
		ServerRequest serverRequest = new DefaultServerRequest(exchange);
		// mediaType
		MediaType mediaType = exchange.getRequest().getHeaders().getContentType();
		// read & modify body
		Mono<String> modifiedBody = serverRequest.bodyToMono(String.class).flatMap(body -> {
			if (MediaType.APPLICATION_FORM_URLENCODED.isCompatibleWith(mediaType)) {
				String bodystr = FromBody(openid, body);
				return Mono.just(bodystr);

			} else if (MediaType.APPLICATION_JSON_UTF8.isCompatibleWith(mediaType)) {
				String bodystr = JsonBody(openid, body);
				return Mono.just(bodystr);
			}
			return Mono.empty();
		});
		BodyInserter bodyInserter = BodyInserters.fromPublisher(modifiedBody, String.class);
		HttpHeaders headers = new HttpHeaders();
		headers.putAll(exchange.getRequest().getHeaders());

		// the new content type will be computed by bodyInserter
		// and then set in the request decorator
		headers.remove(HttpHeaders.CONTENT_LENGTH);

		CachedBodyOutputMessage outputMessage = new CachedBodyOutputMessage(exchange, headers);

		return bodyInserter.insert(outputMessage, new BodyInserterContext()).then(Mono.defer(() -> {
			ServerHttpRequest decorator = decorate(exchange, headers, outputMessage);
			return chain.filter(exchange.mutate().request(decorator).build());
		}));
	}

	private ServerHttpRequestDecorator decorate(ServerWebExchange exchange, HttpHeaders headers,
			CachedBodyOutputMessage outputMessage) {
		return new ServerHttpRequestDecorator(exchange.getRequest()) {
			@Override
			public HttpHeaders getHeaders() {
				long contentLength = headers.getContentLength();
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.putAll(super.getHeaders());
				if (contentLength > 0) {
					httpHeaders.setContentLength(contentLength);
				} else {
					httpHeaders.set(HttpHeaders.TRANSFER_ENCODING, "chunked");
				}
				return httpHeaders;
			}

			@Override
			public Flux<DataBuffer> getBody() {
				return outputMessage.getBody();
			}
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
