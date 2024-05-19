package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.dto.response.CommonResponse;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
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

	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<CommonResponse<?>> dataIntegrityViolationExceptionHandler(DataIntegrityViolationException e) {
		CommonResponse.CommonResponseBuilder<Object> builder = CommonResponse.builder();// gw buat buildernya dulu aja, nanti kita isi, kita buat kondisi dulu
//		CommonResponse.builder(); bisa tulis seperti ini dulu dan command .
		// nanti kalau udah sesuai kondisi, kita tinggal panggil variable buildernya


		HttpStatus httpStatus; // kira buat variable untuk menyimpan httpstatus untuk status di responseEntity

		// kita tangkep messagenya di e.getMessage() dan mengandung kata
		if (e.getMessage().contains("foreign key constraint")) {
			// ini error kalau misalnya kalian menghapus customer tapi udah memiliki transaksi
			builder.statusCode(HttpStatus.BAD_REQUEST.value());
			builder.message("tidak dapat menghapus data karena ada referensi dari tabel lain");
			httpStatus = HttpStatus.BAD_REQUEST;
		} else if (e.getMessage().contains("unique constraint") || e.getMessage().contains("Duplicate entry")) {
			builder.statusCode(HttpStatus.CONFLICT.value());
			builder.message("Data already exist");
			httpStatus = HttpStatus.CONFLICT;
		} else {
			// selain dari ini gw lupa juga errornya apa, ini kita buat jadi Internal Server Error aja
			// tapi nanti kalau di kantor kalian ada penanganan khusus bisa di tambahkan ifnya
			builder.statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
			builder.message("Internal Server Error");
			httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
		}

		return ResponseEntity.status(httpStatus).body(builder.build());
	}
}
