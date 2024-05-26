package com.enigma.enigma_shop.controller;


import com.enigma.enigma_shop.entity.Image;
import com.enigma.enigma_shop.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class TestImageController {

	private final ImageService imageService;

	@PostMapping(path = "/api/v1/test-upload")
	public ResponseEntity<?> testUpload(
					@RequestPart(name = "image")MultipartFile multipartFile
	) {
		Image image = imageService.create(multipartFile);
		return ResponseEntity.status(HttpStatus.CREATED).body(image);
	}

	// url = "/api/products/images/{id}
	//@GetMapping(path = "/api/products/images/{id}") ini untuk di product lagi, jadi search image by product id

	@GetMapping(path = "/api/v1/test-download/{imageId}/")
	public ResponseEntity<?> downloadImage(@PathVariable(name = "imageId") String id){
		Resource resource = imageService.getById(id);
		// "attachment; filename="+resource.getFilename()
		String headerValue = String.format("attachment; filename=%s", resource.getFilename());

		return ResponseEntity
						.status(HttpStatus.OK)
						// ini untuk headernya
						.header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
						.body(resource);
	}
	/**
	 *{
	 *   "prod...": "",
	 *   "image":{
	 *     "url": "/api/v1/test-download/{imageId}/",
	 *     "name": "jwt-gambar.png"
	 *   }
	 *}
	 * // url kenapa enggak pakai domain, karena nanti yg setting frontendnya, karena nanti domainya bisa beda sesui dengan deploy,
	 * jadi kita sebagai backend hanya kirim enpoint aja
	 *
	 * <img src="/api/v1/test-download/{imageId}/"/>
	 * nanti di tambahin sama si frontendnya
	 * <img src="http://localhost:8080/api/v1/test-download/{imageId}/"/>
	 * */
}
