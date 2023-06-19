package com.fullcycle.admin.catalogo.infrastructure.video.persistence;

import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoID;

import java.time.Instant;
import java.time.Year;
import javax.persistence.*;

@Table(name = "videos")
@Entity(name = "Video")
public class VideoJpaEntity {

  @Id
  @Column(name = "id", nullable = false)
  private String id;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "description", length = 4000)
  private String description;

  @Column(name = "year_launched", nullable = false)
  private int yearLaunched;

  @Column(name = "opened", nullable = false)
  private boolean opened;

  @Column(name = "published", nullable = false)
  private boolean published;

  @Column(name = "rating")
  private Rating rating;

  @Column(name = "duration", precision = 2)
  private double duration;

  @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
  private Instant updatedAt;

  public VideoJpaEntity() {}

  private VideoJpaEntity(
      final String id,
      final String title,
      final String description,
      final int yearLaunched,
      final boolean opened,
      final boolean published,
      final Rating rating,
      final double duration,
      final Instant createdAt,
      final Instant updatedAt) {
    this.id = id;
    this.title = title;
    this.description = description;
    this.yearLaunched = yearLaunched;
    this.opened = opened;
    this.published = published;
    this.rating = rating;
    this.duration = duration;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
  }

  public static VideoJpaEntity from(final Video aVideo) {
    return new VideoJpaEntity(
        aVideo.getId().getValue(),
        aVideo.getTitle(),
        aVideo.getDescription(),
        aVideo.getLaunchedAt().getValue(),
        aVideo.getOpened(),
        aVideo.getPublished(),
        aVideo.getRating(),
        aVideo.getDuration(),
        aVideo.getCreatedAt(),
        aVideo.getUpdatedAt());
  }

  public Video toAggregate() {
    return Video.with(
            VideoID.from(getId()),
            getTitle(),
            getDescription(),
            Year.of(getYearLaunched()),
            getDuration(),
            isOpened(),
            isPublished(),
            getRating(),
            getCreatedAt(),
            getUpdatedAt(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
    );
  }

  public String getId() {
    return id;
  }

  public VideoJpaEntity setId(String id) {
    this.id = id;
    return this;
  }

  public String getTitle() {
    return title;
  }

  public VideoJpaEntity setTitle(String title) {
    this.title = title;
    return this;
  }

  public String getDescription() {
    return description;
  }

  public VideoJpaEntity setDescription(String description) {
    this.description = description;
    return this;
  }

  public int getYearLaunched() {
    return yearLaunched;
  }

  public VideoJpaEntity setYearLaunched(int yearLaunched) {
    this.yearLaunched = yearLaunched;
    return this;
  }

  public boolean isOpened() {
    return opened;
  }

  public VideoJpaEntity setOpened(boolean opened) {
    this.opened = opened;
    return this;
  }

  public boolean isPublished() {
    return published;
  }

  public VideoJpaEntity setPublished(boolean published) {
    this.published = published;
    return this;
  }

  public Rating getRating() {
    return rating;
  }

  public VideoJpaEntity setRating(Rating rating) {
    this.rating = rating;
    return this;
  }

  public double getDuration() {
    return duration;
  }

  public VideoJpaEntity setDuration(double duration) {
    this.duration = duration;
    return this;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public VideoJpaEntity setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public VideoJpaEntity setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }


}
