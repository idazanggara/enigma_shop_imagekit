package com.enigma.enigma_shop.service.impl;

import com.enigma.enigma_shop.entity.Image;
import com.enigma.enigma_shop.repository.ImageRepository;
import com.enigma.enigma_shop.service.ImageService;
import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
//@RequiredArgsConstructor khusus untuk service ini kita enggak gunakan
// karena kita butuh patnya daeri .properties
public class ImageServiceImpl implements ImageService {
	private final Path directoryPath;
	private final ImageRepository repository;
	private final ImageRepository imageRepository;

	@Autowired
	public ImageServiceImpl(
					@Value("${enigma_shop.multipart.path_location}") String directoryPath,
					ImageRepository repository,
					ImageRepository imageRepository) {
		this.directoryPath = Paths.get(directoryPath);
		this.repository = repository;
		this.imageRepository = imageRepository;
	}

	@PostConstruct
	public void initDirectory() {
		if(!Files.exists(directoryPath)){
			try {
				Files.createDirectory(directoryPath);
			} catch (IOException e) {
				throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
			}
		}
	}

	@Override
	public Image create(MultipartFile multipartFile) {
		try {
			// setelah semua selesai untuk create
			if (!List.of("image/jpeg", "image/png", "image/jpg", "image/svg+xml").contains(multipartFile.getContentType())){
				throw new ConstraintViolationException("invalid image type", null);
			}


			// simpan dulu di directory
			//String filename = multipartFile.getOriginalFilename();

			String uniqueFilename = System.currentTimeMillis() + "_" + multipartFile.getOriginalFilename();

			// untuk save image ke folder/directory
			// /Gambar Enigma Shop/gambar-wibu.png
			Path filePath = directoryPath.resolve(uniqueFilename);
			// ini untuk menyimpan file/image kita ke path
			// parameter 1: menyimpan binary gambar/file
			// paramter 2: menyimpan lolasi pathnya
			Files.copy(multipartFile.getInputStream() ,filePath);

			// simpan pathnya di database
			// gimana cara simpannya? kita bisa dapet data multipartnya dari product, jadi nanti product kita ubah codingannya yg create
			Image image = Image.builder()
							.name(uniqueFilename)
							.contentType(multipartFile.getContentType())
							.size(multipartFile.getSize())
							//lokasi dari pathnya akan tersimpajn di database
							.path(filePath.toString())
							.build();

			imageRepository.saveAndFlush(image);

			return image;
		}catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	public Resource getById(String id) {
		try {
			Image image = imageRepository
							.findById(id)
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "data not found"));

			// untuk mencarinya kita bisa gunakan path atau alamatnya ya
			Path filePath = Paths.get(image.getPath());

			if (!Files.exists(filePath)){
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "data not found");
			}
			return new UrlResource(filePath.toUri());
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

	@Override
	public void deleteById(String id) {
		try {
			Image image = imageRepository
							.findById(id)
							.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,  "data not found"));

			Path filePath = Paths.get(image.getPath());

			if (!Files.exists(filePath)){
				throw new ResponseStatusException(HttpStatus.NOT_FOUND,  "data not found");
			}
			// untuk delete file di directory
			Files.delete(filePath);
			// untuk delete di database
			imageRepository.delete(image);
		} catch (IOException e) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}
}
