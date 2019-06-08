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

	@RequestMapping("/wechat/pay/order")
	public Mono<String> fallback_pay_order() {
		return Mono.just("支付系统维护中请等待...");
	}

	@RequestMapping("/wechat/pay/refund")
	public Mono<String> fallback_wechat_pay_refund() {
		return Mono.just("退款系统维护中，请等待...");
	}

	@RequestMapping("/wechat/oauth/jwt")
	public Mono<String> fallback_wechat_aouth_jwt() {
		return Mono.just("认证系统维护中，请等待...");
	}

}
