package com.fullcycle.admin.catalogo.infrastructure.api.controllers;

import com.fullcycle.admin.catalogo.application.video.create.CreateVideoCommand;
import com.fullcycle.admin.catalogo.application.video.create.CreateVideoUseCase;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.infrastructure.api.VideoAPI;
import com.fullcycle.admin.catalogo.infrastructure.utils.HashingUtils;
import java.net.URI;
import java.util.Objects;
import java.util.Set;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class VideoController implements VideoAPI {

  private final CreateVideoUseCase createVideoUseCase;

  public VideoController(CreateVideoUseCase createVideoUseCase) {
    this.createVideoUseCase = Objects.requireNonNull(createVideoUseCase);
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
