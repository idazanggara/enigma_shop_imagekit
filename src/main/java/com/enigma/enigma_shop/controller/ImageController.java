package com.enigma.enigma_shop.controller;

import com.enigma.enigma_shop.constant.APIUrl;
import com.enigma.enigma_shop.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
public class ImageController {

	private final ImageService imageService;

	@GetMapping(path = APIUrl.PRODUCT_IMAGE_DOWNLOAD_API + "{imageId}")
	public ResponseEntity<Resource> downloadImage(@PathVariable(name = "imageId") String id) {
		try {
			Resource resource = imageService.getById(id);
			if (!resource.exists() || !resource.isReadable()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Could not read file: " + id);
			}

			String filename = resource.getFilename() != null ? resource.getFilename() : "downloaded_image";
			ContentDisposition contentDisposition = ContentDisposition.builder("attachment")
							.filename(filename)
							.build();
			return ResponseEntity.ok()
							.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
							.body(resource);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"Error: " + e.getMessage());
		}
	}
}
