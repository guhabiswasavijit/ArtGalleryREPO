package org.seven;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class Application {

	public static void main(String[] args) {
		ConfigurableApplicationContext  ctx = SpringApplication.run(Application.class, args);
		ThumbnailGenerator thumbnailGenerator = ctx.getBean(ThumbnailGenerator.class);
		log.info("got thumbnail generator {}",thumbnailGenerator);
		thumbnailGenerator.generateThumbnail();
	}
}