package org.seven;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThumbnailGeneratorService {
	@Autowired
	private ThumbnailGeneratorRepository repository;
	
	public void saveThumbnail(ThumbnailImageData thumnail) {
		this.repository.save(thumnail);
	}

}
