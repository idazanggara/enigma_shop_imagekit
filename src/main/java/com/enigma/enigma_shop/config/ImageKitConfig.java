package com.enigma.enigma_shop.config;

import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.Properties;

@org.springframework.context.annotation.Configuration
public class ImageKitConfig {

	@Value("${imagekit.publicKey}")
	private String privateKey;

	@Value("${imagekit.privateKey}")
	private String publicKey;

	@Value("${imagekit.urlEndpoint}")
	private String urlEndpoint;

	@Bean
	public ImageKit imageKit() {
		Configuration config = new Configuration(publicKey, privateKey, urlEndpoint);
		ImageKit imageKit = ImageKit.getInstance();
		imageKit.setConfig(config);
		return imageKit;
	}

//	@Bean
//	public ImageKit imageKit() throws IOException {
//		Properties props = new Properties();
//		props.load(new ClassPathResource("application.properties").getInputStream());
//
//		String publicKey = props.getProperty("imagekit.publicKey");
//		String privateKey = props.getProperty("imagekit.privateKey");
//		String urlEndpoint = props.getProperty("imagekit.urlEndpoint");
//
//		Configuration config = new Configuration();
//		config.setPublicKey(publicKey);
//		config.setPrivateKey(privateKey);
//		config.setUrlEndpoint(urlEndpoint);
//
//		ImageKit imageKit = ImageKit.getInstance();
//		imageKit.setConfig(config);
//
//		return imageKit;
	}
}
