package com.enigma.enigma_shop.util;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator; // jangan salah import
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

// gimana si util ini di panggil? kita buat jadi component agar di auto wired
// jadi bukan static, kalau static nanti sama terus datanya
@Component
@RequiredArgsConstructor// karena Validator ada sebuah bean, jadi kita bisa autoWired ya (DI)
public class ValidationUtil {
	// jadi nanti disini, itu untuk ngetrigger atau memanggil validasinya ketika client mengirim data yg tidak sesuai

	private final Validator validator;

// nanti DTO nya akan masuk jadi parameter objectnya
	public void validate(Object obj) {
		// disni kenapa Set? karena anotasi validasi kita banyak,
		// jadi sama dia akan di kumpulin menjadi satu
		// set ini apa? harus unik ya
		Set<ConstraintViolation<Object>> validate = validator.validate(obj);// ini untuk triggernya
		// kalau validasinya tidak kosong, maka kita throw si ConstraintViolationException
		if (!validate.isEmpty()) {
			throw new ConstraintViolationException(validate);
		}
	}

	// nah gimana cara kita panggil validate ini di service?


}
