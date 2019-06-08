package org.xarch.reliable.utils.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.util.Date;
import java.util.Map;

import javax.crypto.SecretKey;

import org.apache.commons.codec.binary.Base64;

public class JwtUtils {

	/**
	 * 由字符串生成加密key
	 * 
	 * @return
	 */
	private static SecretKey generalKey(String keyStr) {
		keyStr = keyStr + Constant.JWT_SECRET;
		String key = keyStr.substring(22, 25) + keyStr.substring(31, 33) + keyStr.substring(17, 19)
				+ keyStr.substring(6, 11);
		String stringKey = key + Constant.JWT_SECRET;
		byte[] encodedKey = Base64.decodeBase64(stringKey);
		return Keys.hmacShaKeyFor(encodedKey);
	}

	public static String createJWT(String keyStr, Map<String, Object> claims, String subject) throws Exception {
		SecretKey skey = generalKey(keyStr);
		Date now = new Date();
		Date thirtyMinutes = new Date(System.currentTimeMillis() + Constant.JWT_TTL);

		JwtBuilder builder = Jwts.builder() // 这里其实就是new一个JwtBuilder，设置jwt的body
				.setClaims(claims) // 如果有私有声明，一定要先设置这个自己创建的私有的声明，这个是给builder的claim赋值，一旦写在标准的声明赋值之后，就是覆盖了那些标准的声明的
				.setId(Constant.JWT_ID) // 设置jti(JWT ID)：是JWT的唯一标识，根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击。
				.setIssuer(Constant.JWT_ISSUER) // issuer：jwt签发人
				.setIssuedAt(now) // jwt的签发时间
				.setExpiration(thirtyMinutes) // jwt的过期时间
				.setSubject(subject) // 代表这个JWT的主体，即它的所有人，这个是一个json格式的字符串，可以存放什么userid，roldid之类的，作为什么用户的唯一标志。
				.signWith(skey, Constant.signatureAlgorithm);
		return builder.compact();
	}

	public static Claims parseJWT(String keyStr, String jwt) throws Exception {
		SecretKey skey = generalKey(keyStr);
		try {
			return Jwts.parser().setSigningKey(skey).parseClaimsJws(jwt).getBody();
		} catch (JwtException e) {
			// don't trust the JWT!
			System.out.println("===========");
			System.out.println(e.getMessage());
			System.out.println("===========");
			e.printStackTrace();
		}
		return null;
	}

}
