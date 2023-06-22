package com.fullcycle.admin.catalogo.infrastructure.video;

import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.video.*;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.video.persistence.VideoRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DefaultVideoGateway implements VideoGateway {

  private final VideoRepository videoRepository;

  public DefaultVideoGateway(final VideoRepository videoRepository) {
    this.videoRepository = Objects.requireNonNull(videoRepository);
  }

  @Override
  @Transactional
  public Video create(final Video aVideo) {
    return this.videoRepository.save(VideoJpaEntity.from(aVideo)).toAggregate();
  }

  @Override
  public void deleteById(final VideoID anId) {
    final var aVideoId = anId.getValue();
    if (this.videoRepository.existsById(aVideoId)) {
      this.videoRepository.deleteById(aVideoId);
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<Video> findById(final VideoID anId) {
    return Optional.empty();
  }

  @Override
  @Transactional
  public Video update(final Video aVideo) {
    return null;
  }

  @Override
  public Pagination<Video> findAll(VideoSearchQuery aQuery) {
    return null;
  }
}
