package com.fullcycle.admin.catalogo.infrastructure.video.presenters;

import com.fullcycle.admin.catalogo.application.video.retrieve.get.VideoOutput;
import com.fullcycle.admin.catalogo.application.video.update.UpdateVideoOutput;
import com.fullcycle.admin.catalogo.domain.video.AudioVideoMedia;
import com.fullcycle.admin.catalogo.domain.video.ImageMedia;
import com.fullcycle.admin.catalogo.infrastructure.video.models.*;

public interface VideoApiPresenter {

  static VideoResponse present(final VideoOutput output) {
    return new VideoResponse(
        output.id(),
        output.title(),
        output.description(),
        output.launchedAt(),
        output.duration(),
        output.opened(),
        output.published(),
        output.rating().getName(),
        output.createdAt(),
        output.updatedAt(),
        present(output.banner()),
        present(output.thumbnail()),
        present(output.thumbnailHalf()),
        present(output.video()),
        present(output.trailer()),
        output.categories(),
        output.genres(),
        output.castMembers());
  }

  static AudioVideoMediaResponse present(final AudioVideoMedia media) {
    if (media == null) {
      return null;
    }
    return new AudioVideoMediaResponse(
        media.id(),
        media.checksum(),
        media.name(),
        media.rawLocation(),
        media.encodedLocation(),
        media.status().name());
  }

  static ImageMediaResponse present(final ImageMedia image) {
    if (image == null) {
      return null;
    }
    return new ImageMediaResponse(image.id(), image.checksum(), image.name(), image.location());
  }

  static UpdateVideoResponse present(final UpdateVideoOutput output) {
    return new UpdateVideoResponse(output.id());
  }
}
