package com.enigma.enigma_shop.constant;

public enum UserRole {
	ROLE_SUPER_ADMIN, //0
	ROLE_ADMIN, //1
	ROLE_CUSTOMER //2
}

// nah kepana gw kasih ROLE_ di depannya,
// karena rolenya itu prefixnya ROLE_ dari spring securitynya,
// jadi cara kerjanya si spring security akan baca role dengan prefix ROLE_
