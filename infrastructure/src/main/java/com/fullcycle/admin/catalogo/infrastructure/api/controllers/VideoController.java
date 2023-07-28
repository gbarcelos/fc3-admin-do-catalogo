package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.delete.DeleteVideoUseCase;
import com.fullcycle.admin.catalogo.application.video.retrieve.get.GetVideoByIdUseCase;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.infrastructure.api.VideoAPI;
import com.fullcycle.admin.catalogo.infrastructure.utils.HashingUtils;
import com.fullcycle.admin.catalogo.infrastructure.video.models.CreateVideoRequest;
import com.fullcycle.admin.catalogo.infrastructure.video.models.UpdateVideoRequest;
import com.fullcycle.admin.catalogo.infrastructure.video.models.VideoResponse;
import com.fullcycle.admin.catalogo.infrastructure.video.presenters.VideoApiPresenter;
import java.net.URI;
import java.util.Objects;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class VideoController implements VideoAPI {

  private final CreateVideoUseCase createVideoUseCase;
  private final GetVideoByIdUseCase getVideoByIdUseCase;
  private final UpdateVideoUseCase updateVideoUseCase;
  private final DeleteVideoUseCase deleteVideoUseCase;

  public VideoController(
      final CreateVideoUseCase createVideoUseCase,
      final GetVideoByIdUseCase getVideoByIdUseCase,
      final UpdateVideoUseCase updateVideoUseCase,
      final DeleteVideoUseCase deleteVideoUseCase) {
    this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
    this.getVideoByIdUseCase = Objects.requireNonNull(getVideoByIdUseCase);
    this.updateVideoUseCase = Objects.requireNonNull(updateVideoUseCase);
    this.deleteVideoUseCase = Objects.requireNonNull(deleteVideoUseCase);
  }

  @Override
  public ResponseEntity<?> createFull(
      final String aTitle,
      final String aDescription,
      final Integer launchedAt,
      final Double aDuration,
      final Boolean wasOpened,
      final Boolean wasPublished,
      final String aRating,
      final Set<String> categories,
      final Set<String> castMembers,
      final Set<String> genres,
      final MultipartFile videoFile,
      final MultipartFile trailerFile,
      final MultipartFile bannerFile,
      final MultipartFile thumbFile,
      final MultipartFile thumbHalfFile) {
    final var aCmd =
        CreateVideoCommand.with(
            aTitle,
            aDescription,
            launchedAt,
            aDuration,
            wasOpened,
            wasPublished,
            aRating,
            categories,
            genres,
            castMembers,
            resourceOf(videoFile),
            resourceOf(trailerFile),
            resourceOf(bannerFile),
            resourceOf(thumbFile),
            resourceOf(thumbHalfFile));

    final var output = this.createVideoUseCase.execute(aCmd);

    return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
  }

  @Override
  public ResponseEntity<?> createPartial(final CreateVideoRequest payload) {
    final var aCmd =
        CreateVideoCommand.with(
            payload.title(),
            payload.description(),
            payload.yearLaunched(),
            payload.duration(),
            payload.opened(),
            payload.published(),
            payload.rating(),
            payload.categories(),
            payload.genres(),
            payload.castMembers());

    final var output = this.createVideoUseCase.execute(aCmd);

    return ResponseEntity.created(URI.create("/videos/" + output.id())).body(output);
  }

  @Override
  public VideoResponse getById(final String anId) {
    return VideoApiPresenter.present(this.getVideoByIdUseCase.execute(anId));
  }

  @Override
  public ResponseEntity<?> update(final String id, final UpdateVideoRequest payload) {
    final var aCmd =
        UpdateVideoCommand.with(
            id,
            payload.title(),
            payload.description(),
            payload.yearLaunched(),
            payload.duration(),
            payload.opened(),
            payload.published(),
            payload.rating(),
            payload.categories(),
            payload.genres(),
            payload.castMembers());

    final var output = this.updateVideoUseCase.execute(aCmd);

    return ResponseEntity.ok()
        .location(URI.create("/videos/" + output.id()))
        .body(VideoApiPresenter.present(output));
  }

  @Override
  public void deleteById(final String id) {
    this.deleteVideoUseCase.execute(id);
  }

  private Resource resourceOf(final MultipartFile part) {
    if (part == null) {
      return null;
    }

    try {
      return Resource.with(
          part.getBytes(),
          HashingUtils.checksum(part.getBytes()),
          part.getContentType(),
          part.getOriginalFilename());
    } catch (Throwable t) {
      throw new RuntimeException(t);
    }
  }
}
