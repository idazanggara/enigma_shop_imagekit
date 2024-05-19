package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.dto.request.AuthRequest;
import com.enigma.enigma_shop.dto.response.CommonResponse;
import com.enigma.enigma_shop.dto.response.LoginResponse;
import com.enigma.enigma_shop.dto.response.RegisterResponse;
import com.enigma.enigma_shop.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = APIUrl.AUTH_API)
@RequiredArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PostMapping(path = "/register",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	// untuk paramternya bisa pakai UserAccount entitty? bisa ya tapi nanti kan isEnable bukan inputan client ya
	public ResponseEntity<CommonResponse<?>> registerUser(@RequestBody AuthRequest request){
		// disini kita panggil service untuk ngirim request ya
		// return null; dulu
		RegisterResponse register = authService.register(request);
		CommonResponse<RegisterResponse> response = CommonResponse.<RegisterResponse>builder()
						.statusCode(HttpStatus.CREATED.value())
						.message("successfully save data")
						.data(register)
						.build();
		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PostMapping(path = "/login",
					consumes = MediaType.APPLICATION_JSON_VALUE,
					produces = MediaType.APPLICATION_JSON_VALUE
	)
	public ResponseEntity<CommonResponse<?>> login(@RequestBody AuthRequest request) {
		// login satu api ya, jadi mau login customer, admin, atau super_admin
		LoginResponse loginResponse = authService.login(request);
		CommonResponse<LoginResponse> response = CommonResponse.<LoginResponse>builder()
						.statusCode(HttpStatus.OK.value())
						.message("login successfully")
						.data(loginResponse)
						.build();
		return ResponseEntity.ok(response);
	}

}
