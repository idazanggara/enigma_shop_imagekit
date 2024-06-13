package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.entity.Image;
import com.enigma.enigma_shop.repository.ImageRepository;
import com.enigma.enigma_shop.service.ImageService;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ImageKitServiceImpl implements ImageService {

	private final ImageRepository imageRepository;
	private final ImageKit imageKit;

//	public ImageKitServiceImpl(ImageRepository imageRepository, ImageKit imageKit) {
//		this.imageRepository = imageRepository;
//		this.imageKit = imageKit;
//	}

	@Override
	public Image create(MultipartFile multipartFile) {
		try {
			String filename = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();
			FileCreateRequest fileCreateRequest = new FileCreateRequest(multipartFile.getBytes(), filename);

			Result result = imageKit.upload(fileCreateRequest);

			Image image = new Image();
			image.setName(filename);
			image.setPath(result.getUrl());
			image.setSize(multipartFile.getSize());
			image.setContentType(multipartFile.getContentType());

			return imageRepository.save(image);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to upload image", e);
		}
	}

//	@Override
//	public Resource getById(String id) {
//		Image image = imageRepository.findById(id)
//						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
//		try {
//			return new UrlResource(image.getPath());
//		} catch (Exception e) {
//			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve image", e);
//		}
//	}

	@Override
	public Resource getById(String id) {
		Image image = imageRepository.findById(id)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));
		try {
			// Ensure the path is a full URL
			URL imageUrl = new URL(image.getPath());  // Assuming image.getPath() gives a direct URL
			Resource resource = new UrlResource(imageUrl);

			if (resource.exists() || resource.isReadable()) {
				return resource;
			} else {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The image file was not found or not readable");
			}
		} catch (MalformedURLException e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "URL is malformed", e);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve image", e);
		}
	}


	@Override
	public void deleteById(String id) {
		Image image = imageRepository.findById(id)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));

		if (image.getId() != null) {
			try {
				imageKit.deleteFile(image.getId());
			} catch (Exception e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to delete image from ImageKit", e);
			}
		}

		imageRepository.delete(image);
	}
}
