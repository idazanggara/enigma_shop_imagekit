package com.enigma.enigma_shop.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.enigma.enigma_shop.dto.response.JwtClaims;
import com.enigma.enigma_shop.entity.UserAccount;
import com.enigma.enigma_shop.service.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;

@Service
// enggak ada DB dan Repository jadi enggak perlu DI ya jadi enggak perlu anotasi
//@RequiredArgsConstructor
@Slf4j
public class JwtServiceImpl implements JwtService {
	private final String JWT_SECRET;
	private final String ISSUER;
	private final long JWT_EXPIRATION;

	public JwtServiceImpl(
					@Value("${enigma_shop.jwt.secret_key}") String jwtSecret,
					@Value("${enigma_shop.jwt.issuer}") String issuer,
					@Value("${enigma_shop.jwt.expirationInSecond}") long expiration
	){
		JWT_SECRET = jwtSecret;
		ISSUER = issuer;
		JWT_EXPIRATION = expiration;
	}

	@Override
	public String generateToken(UserAccount account) {
		try {
			// kita masukkan secretKey di argumentnya, tapi sekalian kita pakai env
			// nah sekarang gimana kita gunain secret key dari aplikation properties
			Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET); // ini buat algoritmanya, HMAC512 ini sama dengan HS512
			return JWT.create()
							.withIssuedAt(Instant.now()) // kapan di buatnya
							// instan ini yg paling gampang, karean udah ada logic untuk plus
							// instant.now() itu waktu sekarang dan ditambah berapa detik
							.withExpiresAt(Instant.now().plusSeconds(JWT_EXPIRATION)) // 60dtk * 15 = 15 menit
							.withIssuer(ISSUER)
							// setelah login selesai dan JWTnya masih kurang komplit
//							.withSubject("?")
//							.withClaim("roles","?") // ini buat property sendiri
							.withSubject(account.getId())
							// GrantedAuthority: {authority: ROLE_SUPER_ADMIN}
							// getternya getAuthority, jadi kita panggil getternya dan dia kan mengembalikan string.
//							.withClaim("roles", account.getAuthorities().stream().map(role -> role.getAuthority()).toList())
							.withClaim("roles", account.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
							.sign(algorithm); // ini duluan
			// nah ini di coba dulu

		} catch (JWTCreationException e) {
			// status codenya gw enggak tau, jadi kasih INTERNAL_SERVER_ERROR
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "error while creating jwt token");
		}
	}

	@Override
	public boolean verifyJwtToken(String beareToken) {
//		String sampleToken = "Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MTYxMTQzNTIsImV4cCI6MTcxNjExNTI1MiwiaXNzIjoiRW5pZ21hIFNob3AiLCJzdWIiOiIwODJhZGI5ZC0wODFlLTRjODgtYjMzOS05MWE5NmJhY2NkNmMiLCJyb2xlcyI6WyJST0xFX0NVU1RPTUVSIl19.Cbnlb73bi1BnefKezmIIq_otnO62Adtj2MrJ0cRoiMQwYuSBymhTcLUOQAZ1Cs5NXp2ZjXF7VVAU8CCLinfvbw";
		try {
			Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET); // samai dengan saat kita buat

			JWTVerifier jwtVerifier = JWT.require(algorithm)
							.withIssuer(ISSUER)
							.build();
			// kiota coba simulasikan token dengan string
			jwtVerifier.verify(parseJwt(beareToken));
			return true;
		}	catch (JWTVerificationException exception){
			// kita pakai log aja, enggak kita lempar kalau error
			// masih inget log kan, pakai Anotasi ya
			log.error("Invalid JWT Signature/Claims : {} ", exception.getMessage());
			return false;
		}
	}

	@Override
	public JwtClaims getClaimsByToken(String beareToken) {
		// ini logicnya sama denga verify jadi sekalian copas aja, karena nanti ini juga perlu
		try {
			Algorithm algorithm = Algorithm.HMAC512(JWT_SECRET);
			JWTVerifier jwtVerifier = JWT.require(algorithm)
							.withIssuer(ISSUER)
							.build();
			DecodedJWT decodedJWT = jwtVerifier.verify(parseJwt(beareToken));
			return JwtClaims.builder()
							.userAccountId(decodedJWT.getSubject())
							.roles(decodedJWT.getClaim("roles").asList(String.class))
							.build();
		}	catch (JWTVerificationException exception){
			log.error("Invalid JWT Signature/Claims : {} ", exception.getMessage());
			return null;
		}
	}

	private String parseJwt(String token) {
		// kalau tokennya enggak kosong, sama kalau tokennya itu mulai dari
		if (token != null && token.startsWith("Bearer ")) {
			// ini untuk mengambil data string sesuai yg kita mau
			// kalau disini kita pengen ubah dengan data pertama itu seletah index berapa gitu
			// disini kita tulis 7, karena kita mau mulai dari char ke 8. dan char 0 s/d 7 enggak kita pakai
			return token.substring(7);
		}
		return null;
	}
}
