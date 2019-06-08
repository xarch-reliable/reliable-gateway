package org.xarch.reliable.utils.jwt;

import java.util.UUID;

import io.jsonwebtoken.SignatureAlgorithm;

public class Constant {
	/**
	 * JWT ID
	 */
	public static final String JWT_ID = UUID.randomUUID().toString();

	/**
	 * 加密密文
	 */
	public static final String JWT_SECRET = "kIyUXLw7jh2y4OYDVs8I7Bsy1MTmVwY5hzk2PWHJ";
	/**
	 * 签发者
	 */
	public static final String JWT_ISSUER = "OAUTH";
	/**
	 * 加密算法
	 */
	public static final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
	/**
	 * 有效时长
	 */
	public static final int JWT_TTL = 60 * 60 * 1000; // millisecond
}
