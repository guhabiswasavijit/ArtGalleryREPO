package org.seven;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.imageio.ImageIO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import net.coobird.thumbnailator.Thumbnailator;

@Component
public class ThumbnailGenerator {
	@Value("${image.path}")
	private String imagePath;
	@Autowired
	private ThumbnailGeneratorService thumbnailGeneratorService;
	@Scheduled(cron = "0 15 10 15 * ?")
	public void generateThumbnail() {
		File thumbnailDir = new File(imagePath);
		Map<String,String> imageProperties = new HashMap<String,String>();
		if(thumbnailDir.isDirectory()) {
			List<File> thumnailList = Arrays.asList(thumbnailDir.listFiles());
			thumnailList.forEach(file ->{
				BufferedImage image;
				try {
					image = ImageIO.read(file);
					Arrays.stream(image.getPropertyNames()).forEach(propertyName ->{
						imageProperties.put(propertyName,(String)image.getProperty(propertyName));
					});
					BufferedImage thumbnailImage = Thumbnailator.createThumbnail(image,480,600);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
				    ImageIO.write(thumbnailImage, "jpeg",bos);
				    byte [] data = bos.toByteArray();
				    ThumbnailImageData imgEntity = new ThumbnailImageData();
				    String imgPropJsonStr = new Gson().toJson(imageProperties);
				    imgEntity.setImageProperty(imgPropJsonStr.getBytes());
				    imgEntity.setImageData(data);
				    thumbnailGeneratorService.saveThumbnail(imgEntity);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			});
		}

	}


}
