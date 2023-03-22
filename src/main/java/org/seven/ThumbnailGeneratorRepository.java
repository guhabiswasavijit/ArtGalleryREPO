package org.seven;

import java.util.Optional;

import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public class ThumbnailGeneratorRepository extends SimpleJpaRepository<ThumbnailImageData, Integer>   {
	public ThumbnailGeneratorRepository(Class<ThumbnailImageData> domainClass, EntityManager em) {
		super(domainClass, em);
	}
	@Override
	public Page<ThumbnailImageData> findAll(Pageable pageable) {
		return this.findAll(pageable);
	}
	@Override
	public <S extends ThumbnailImageData> S save(S entity) {
		return this.save(entity);
	}
	@Override
	public Optional<ThumbnailImageData> findById(Integer id) {
		return this.findById(id);
	}

}
