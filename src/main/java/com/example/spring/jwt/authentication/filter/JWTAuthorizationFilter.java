package com.example.spring.jwt.authentication.filter;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.StringTokenizer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.thymeleaf.util.StringUtils;

import com.example.spring.jwt.Application;
import com.example.spring.jwt.authentication.dto.AuthenticationToken;
import com.example.spring.jwt.authentication.exception.TokenIllegalException;
import com.example.spring.jwt.authentication.exception.TokenNotfoundException;
import com.example.spring.jwt.authentication.properties.AuthProperties;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * 認証フィルター.
 */
@Slf4j
public class JWTAuthorizationFilter
		extends BasicAuthenticationFilter {

	public static final String DEFAULT_PUBLIC_KEY_RESOURCE = "/keys/id_rsa.pub";

	final AuthProperties authProperties;

	byte[] signingKey;

	RSAPublicKey publicKey;

	/**
	 * 例外エントリを指定したコンストラクタ.
	 * 
	 * @param manager        認証管理
	 * @param entryPoint     例外エントリ
	 * @param authProperties 認証設定
	 */
	public JWTAuthorizationFilter(
			AuthenticationManager manager,
			AuthenticationEntryPoint entryPoint,
			AuthProperties authProperties) {

		super(manager, entryPoint);
		log.debug("<INIT>({},{},{})", manager, entryPoint, authProperties);
		this.authProperties = authProperties;
		log.debug("data = {}, hash {}", authProperties, authProperties.hashCode());
	}

	@Override
	protected void initFilterBean() throws ServletException {
		super.initFilterBean();
		log.debug("initFilterBean()");

		try (InputStream fis = publicKeyIpnputStream();
				DataInputStream dis = new DataInputStream(fis);) {

			signingKey = new byte[fis.available()];
			dis.readFully(signingKey);

			String signingKeyStr = new String(signingKey, StandardCharsets.UTF_8);
			StringTokenizer stoken = new StringTokenizer(signingKeyStr, "\n\r");
			StringBuilder builder = new StringBuilder();
			while (stoken.hasMoreTokens()) {
				String str = stoken.nextToken();
				switch (str) {
				case "-----BEGIN PUBLIC KEY-----":
				case "-----END PUBLIC KEY-----":
					log.debug(str);
					break;

				default:
					log.debug(str);
					builder.append(str);
					break;
				}
			}
			signingKey = Base64.getDecoder().decode(builder.toString());

			X509EncodedKeySpec spec = new X509EncodedKeySpec(signingKey);
			KeyFactory kf = KeyFactory.getInstance("RSA");

			publicKey = (RSAPublicKey) kf.generatePublic(spec);

		} catch (Exception e) {
			throw new ServletException(e.getMessage(), e);
		}
	}

	@Override
	protected void doFilterInternal(
			HttpServletRequest request,
			HttpServletResponse response,
			FilterChain chain)
			throws IOException, ServletException {
		log.debug("doFilterInternal({},{},{})", request, response, chain);

		try {

			String token = request.getHeader(authProperties.getHeaderToken());

			log.debug("token => {}", token);

			if (token == null) {
				throw new TokenNotfoundException();
			}

			SecurityContextHolder.getContext().setAuthentication(authentication(token));
			chain.doFilter(request, response);

		} catch (AuthenticationException e) {
			log.warn(e.getMessage(), e);

			AuthenticationEntryPoint endPoint = getAuthenticationEntryPoint();
			log.debug("endPoint {}", endPoint);

			if (endPoint != null) {
				log.debug("endPoint {} {}", endPoint, "exec");
				endPoint.commence(request, response, e);
			}
		}
	}

	private AuthenticationToken authentication(String token) {
		log.debug("authentication({})", token);

		// parse the token.
		Claims claims = Jwts.parser()
				.setSigningKey(publicKey)
				.parseClaimsJws(token)
				.getBody();

		String user = claims.getSubject();

		if (user == null) {
			throw new TokenIllegalException();
		}

		AuthenticationToken authenticationToken = new AuthenticationToken(user);
		log.debug("data = {}, hash {}", authenticationToken, authenticationToken.hashCode());

		return authenticationToken;
	}

	private InputStream publicKeyIpnputStream() throws IOException {
		String path = authProperties.getPublicKeyPath();
		if (!StringUtils.isEmpty(path)) {
			return new FileInputStream(path);
		}
		return Application.class.getResourceAsStream(DEFAULT_PUBLIC_KEY_RESOURCE);
	}

}
