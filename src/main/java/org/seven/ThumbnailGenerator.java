package org.seven;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import javax.imageio.ImageIO;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnailator;

@Component
@Slf4j
public class ThumbnailGenerator {
	@Value("${image.thumbnails.path}")
	private String imageInputPath;
	@Value("${image.upload.path}")
	private String imageUploadPath;
	@Value("${image.uploadUrl.prefix}")
	private String imageURIPrefix;
	@Autowired
	private ThumbnailGeneratorService thumbnailGeneratorService;
	private static final String DEFAULT_FILE_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
	
	private static ThreadLocal<SimpleDateFormat> dateFormatThLocal = new ThreadLocal<SimpleDateFormat>() {
	    @Override
	    protected SimpleDateFormat initialValue() {
	        return new SimpleDateFormat(DEFAULT_FILE_PATTERN);
	    }
	};
	private static final AtomicLong timestamp = new AtomicLong(System.currentTimeMillis());
	public static String createFileName() {
		Timestamp stamp = new Timestamp(System.currentTimeMillis()+timestamp.addAndGet(100));
		Date today = new Date(stamp.getTime());
	    String todayStr  = dateFormatThLocal.get().format(today);
	    todayStr = todayStr.replaceAll("\\s+","");
	    todayStr = todayStr.replaceAll("\\.","");
	    todayStr = todayStr.replaceAll("\\-","");
	    todayStr = todayStr.replaceAll("\\:","");
	    return todayStr;
	}
	@Scheduled(fixedRate = 100)
	public void generateThumbnail() {
		log.info("about to run job {}",new Date());
		File thumbnailDir = new File(imageInputPath);
		Map<String,String> imageProperties = new HashMap<String,String>();
		if(thumbnailDir.isDirectory()) {
			List<File> thumnailList = Arrays.asList(thumbnailDir.listFiles());
			thumnailList.forEach(file ->{
				log.info("about to process file {}",file);
				BufferedImage image;
				try {
					image = ImageIO.read(file);
					if(image.getPropertyNames() != null) {
						Arrays.stream(image.getPropertyNames()).forEach(propertyName ->{
							imageProperties.put(propertyName,(String)image.getProperty(propertyName));
						});
					}
					BufferedImage thumbnailImage = Thumbnailator.createThumbnail(image,480,600);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
				    ImageIO.write(thumbnailImage,"jpeg",bos);
				    byte [] data = bos.toByteArray();
				    StringBuffer generatedFileName = new StringBuffer();
				    generatedFileName.append(createFileName());
				    generatedFileName.append(".");
				    generatedFileName.append(FilenameUtils.getExtension(file.getName()));
				    imageProperties.put("IMAGE_FILE_NAME",generatedFileName.toString());
				    ThumbnailImageData imgEntity = new ThumbnailImageData();
				    imgEntity.setImageData(data);
				    imgEntity.setImageUploadUrl(imageURIPrefix+generatedFileName);
				    if(!imageProperties.isEmpty()) {
					    String imgPropJsonStr = new Gson().toJson(imageProperties);
					    imgEntity.setImageProperty(imgPropJsonStr.getBytes());
				    }
				    thumbnailGeneratorService.saveThumbnail(imgEntity);
				    StringBuffer fileUploadPath = new StringBuffer();
				    fileUploadPath.append(imageUploadPath);
				    fileUploadPath.append(generatedFileName);
				    log.info("about to upload file {}",fileUploadPath);
				    File fileCopied = new File(fileUploadPath.toString());
				    FileUtils.moveFile(file,fileCopied);
				    log.info("file moved {}",fileCopied);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}

	}


}
