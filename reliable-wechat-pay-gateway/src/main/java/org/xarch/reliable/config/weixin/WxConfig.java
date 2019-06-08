package org.xarch.reliable.config.weixin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

@Configuration // 配置类
@RefreshScope // bus动态刷新
public class WxConfig {

	@Value("${wechat.appid:error}")
	private String appId;

	@Value("${wechat.appsecret:error}")
	private String appseCret;
	
	@Value("${wechat.jwt.key:error}")
	private String jwtKey;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAppseCret() {
		return appseCret;
	}

	public void setAppseCret(String appseCret) {
		this.appseCret = appseCret;
	}

	public String getJwtKey() {
		return jwtKey;
	}

	public void setJwtKey(String jwtKey) {
		this.jwtKey = jwtKey;
	}

}
