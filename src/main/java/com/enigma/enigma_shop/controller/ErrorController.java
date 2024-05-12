package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.dto.response.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

@RestControllerAdvice
public class ErrorController  {

	@ExceptionHandler({ResponseStatusException.class}) // ini digunakan agar ketika ada yg throw, method ini kepanggil.
	// ? ini bisa kirim apapun, String, atau lain lain atau object atau apapun tipedatanya
	// kenapa enggak object, karena ini apapun. enggak cuman object
	// ResponseEntity<Object>    // <?> == Object class
//	public ResponseEntity<?> responseStatusExceptionHandler(ResponseStatusException exception){
//		return  ResponseEntity
//						.status(exception.getStatusCode())// ini bisa kita ambil yg kita throw tadi
//						.body(exception.getReason()); // ini pesannya, tapi bukan dari getMessage melainkan dari getReason ya, karena tadi kita throw reasonnya
//	}

	// SETELAH BUAT DTO CommonResponse
	public ResponseEntity<CommonResponse<?>> responseStatusExceptionHandler(ResponseStatusException exception) {
		// <?> == Object class
//		CommonResponse<?> response = CommonResponse.<String>builder().build();
		// tapi kalau enggak dikasih enggak masalah
		CommonResponse<?> response = CommonResponse.builder()
						.statusCode(exception.getStatusCode().value())// karena enum, disini mau ambil valuenya
						.message(exception.getReason())
						.build();

		return ResponseEntity
						.status(exception.getStatusCode())
						.body(response);
	}


	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<CommonResponse<?>> constraintViolationExceptionHandler(ConstraintViolationException e) {
		CommonResponse<?> response = CommonResponse.builder()
						.statusCode(HttpStatus.BAD_REQUEST.value()) // 400, ini kita panggil sendiri, karena dari validatornya enggak kirim status codenya ya
						.message(e.getMessage())
						.build();
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	}
}
