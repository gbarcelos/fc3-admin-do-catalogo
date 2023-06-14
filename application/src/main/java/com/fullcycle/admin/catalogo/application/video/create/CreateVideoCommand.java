package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.video.Resource;
import java.util.Set;

public record CreateVideoCommand(
    String title,
    String description,
    Integer launchedAt,
    Double duration,
    Boolean opened,
    Boolean published,
    String rating,
    Set<String> categories,
    Set<String> genres,
    Set<String> members,
    Resource video,
    Resource trailer,
    Resource banner,
    Resource thumbnail,
    Resource thumbnailHalf) {

  public static CreateVideoCommand with(
      final String title,
      final String description,
      final Integer launchedAt,
      final Double duration,
      final Boolean opened,
      final Boolean published,
      final String rating,
      final Set<String> categories,
      final Set<String> genres,
      final Set<String> members,
      final Resource video,
      final Resource trailer,
      final Resource banner,
      final Resource thumbnail,
      final Resource thumbnailHalf) {
    return new CreateVideoCommand(
        title,
        description,
        launchedAt,
        duration,
        opened,
        published,
        rating,
        categories,
        genres,
        members,
        video,
        trailer,
        banner,
        thumbnail,
        thumbnailHalf);
  }
}
