package org.xarch.reliable.controller.gateway.hystrix;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

	@RequestMapping("/default")
	public Mono<String> fallback() {
		return Mono.just("维护中...");
	}

	@RequestMapping("/wechat/oauth")
	public Mono<String> fallback_oauth() {
		return Mono.just("oauth系统维护中请等待...");
	}
	
	@RequestMapping("/wechat/messenger")
	public Mono<String> fallback_wechat_aouth_messenger() {
		return Mono.just("messenger系统维护中，请等待...");
	}
	
}
