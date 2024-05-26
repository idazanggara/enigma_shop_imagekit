package com.enigma.enigma_shop.service;

import com.enigma.enigma_shop.entity.Image;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
	Image create(MultipartFile multipartFile);

	// untuk kembalian ketika kita ingin mendapatkan image, yg di return bukan image, tapi Resource
	// Resource dari core.io. resource buat apa? resource ini akan mengembalikan object dalam bentuk gambar
	Resource getById(String id);

	void deleteById(String id);
}
