package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.dto.response.JwtClaims;
import com.enigma.enigma_shop.entity.UserAccount;

public interface JwtService {
	// untuk buat token
//	String generateToken();

	String generateToken(UserAccount userAccount);
	// untuk check token
	boolean verifyJwtToken(String token);
	// ambil data dari jwt menjadi DTO
	// kita buat DTOnya dulu -> JwtClaims
	JwtClaims getClaimsByToken(String token);
//	{
//		"sub": "dfaef43232", UserAccountId
//		"roles": [
//						"ROLE_CUSTOMER"
//						]
//	}
}
