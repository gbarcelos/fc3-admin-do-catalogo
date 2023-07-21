package com.fullcycle.admin.catalogo.domain.video;

import static org.junit.jupiter.api.Assertions.*;

import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.utils.InstantUtils;
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;
import java.time.Year;
import java.util.Set;
import org.junit.jupiter.api.Test;

public class VideoTest {

  @Test
  public void givenValidParams_whenCallsNewVideo_shouldInstantiate() {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription =
        """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());

    // when
    final var actualVideo =
        Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers);

    // then
    assertNotNull(actualVideo);
    assertNotNull(actualVideo.getId());
    assertNotNull(actualVideo.getCreatedAt());
    assertNotNull(actualVideo.getUpdatedAt());
    assertEquals(actualVideo.getCreatedAt(), actualVideo.getUpdatedAt());
    assertEquals(expectedTitle, actualVideo.getTitle());
    assertEquals(expectedDescription, actualVideo.getDescription());
    assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    assertEquals(expectedDuration, actualVideo.getDuration());
    assertEquals(expectedOpened, actualVideo.getOpened());
    assertEquals(expectedPublished, actualVideo.getPublished());
    assertEquals(expectedRating, actualVideo.getRating());
    assertEquals(expectedCategories, actualVideo.getCategories());
    assertEquals(expectedGenres, actualVideo.getGenres());
    assertEquals(expectedMembers, actualVideo.getCastMembers());
    assertTrue(actualVideo.getVideo().isEmpty());
    assertTrue(actualVideo.getTrailer().isEmpty());
    assertTrue(actualVideo.getBanner().isEmpty());
    assertTrue(actualVideo.getThumbnail().isEmpty());
    assertTrue(actualVideo.getThumbnailHalf().isEmpty());
    assertTrue(actualVideo.getDomainEvents().isEmpty());

    assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsUpdate_shouldReturnUpdated() throws InterruptedException {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription =
        """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedEvent = new VideoMediaCreated("ID", "file");
    final var expectedEventCount = 1;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());

    final var aVideo =
        Video.newVideo(
            "Test title",
            "Lalala description",
            Year.of(1888),
            0.0,
            true,
            true,
            Rating.AGE_10,
            Set.of(),
            Set.of(),
            Set.of());

    aVideo.registerEvent(expectedEvent);

    Thread.sleep(1);

    // when
    final var actualVideo =
        Video.with(aVideo)
            .update(
                expectedTitle,
                expectedDescription,
                expectedLaunchedAt,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers);

    // then
    assertNotNull(actualVideo);
    assertNotNull(actualVideo.getId());
    assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
    assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    assertEquals(expectedTitle, actualVideo.getTitle());
    assertEquals(expectedDescription, actualVideo.getDescription());
    assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    assertEquals(expectedDuration, actualVideo.getDuration());
    assertEquals(expectedOpened, actualVideo.getOpened());
    assertEquals(expectedPublished, actualVideo.getPublished());
    assertEquals(expectedRating, actualVideo.getRating());
    assertEquals(expectedCategories, actualVideo.getCategories());
    assertEquals(expectedGenres, actualVideo.getGenres());
    assertEquals(expectedMembers, actualVideo.getCastMembers());
    assertTrue(actualVideo.getVideo().isEmpty());
    assertTrue(actualVideo.getTrailer().isEmpty());
    assertTrue(actualVideo.getBanner().isEmpty());
    assertTrue(actualVideo.getThumbnail().isEmpty());
    assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    assertEquals(expectedEventCount, actualVideo.getDomainEvents().size());
    assertEquals(expectedEvent, actualVideo.getDomainEvents().get(0));

    assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsUpdateVideoMedia_shouldReturnUpdated()
      throws InterruptedException {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription =
        """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var expectedDomainEventSize = 1;

    final var aVideo =
        Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers);

    final var aVideoMedia = AudioVideoMedia.with("abc", "Video.mp4", "/123/videos");

    Thread.sleep(1);

    // when
    final var actualVideo = Video.with(aVideo).updateVideoMedia(aVideoMedia);

    // then
    assertNotNull(actualVideo);
    assertNotNull(actualVideo.getId());
    assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
    assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    assertEquals(expectedTitle, actualVideo.getTitle());
    assertEquals(expectedDescription, actualVideo.getDescription());
    assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    assertEquals(expectedDuration, actualVideo.getDuration());
    assertEquals(expectedOpened, actualVideo.getOpened());
    assertEquals(expectedPublished, actualVideo.getPublished());
    assertEquals(expectedRating, actualVideo.getRating());
    assertEquals(expectedCategories, actualVideo.getCategories());
    assertEquals(expectedGenres, actualVideo.getGenres());
    assertEquals(expectedMembers, actualVideo.getCastMembers());
    assertEquals(aVideoMedia, actualVideo.getVideo().get());
    assertTrue(actualVideo.getTrailer().isEmpty());
    assertTrue(actualVideo.getBanner().isEmpty());
    assertTrue(actualVideo.getThumbnail().isEmpty());
    assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    assertEquals(expectedDomainEventSize, actualVideo.getDomainEvents().size());

    final var actualEvent = (VideoMediaCreated) actualVideo.getDomainEvents().get(0);
    assertEquals(aVideo.getId().getValue(), actualEvent.resourceId());
    assertEquals(aVideoMedia.rawLocation(), actualEvent.filePath());
    assertNotNull(actualEvent.occurredOn());

    assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsUpdateTrailerMedia_shouldReturnUpdated()
      throws InterruptedException {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription =
        """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());
    final var expectedDomainEventSize = 1;

    final var aVideo =
        Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers);

    final var aTrailerMedia = AudioVideoMedia.with("abc", "Trailer.mp4", "/123/videos");

    Thread.sleep(1);
    // when
    final var actualVideo = Video.with(aVideo).updateTrailerMedia(aTrailerMedia);

    // then
    assertNotNull(actualVideo);
    assertNotNull(actualVideo.getId());
    assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
    assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    assertEquals(expectedTitle, actualVideo.getTitle());
    assertEquals(expectedDescription, actualVideo.getDescription());
    assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    assertEquals(expectedDuration, actualVideo.getDuration());
    assertEquals(expectedOpened, actualVideo.getOpened());
    assertEquals(expectedPublished, actualVideo.getPublished());
    assertEquals(expectedRating, actualVideo.getRating());
    assertEquals(expectedCategories, actualVideo.getCategories());
    assertEquals(expectedGenres, actualVideo.getGenres());
    assertEquals(expectedMembers, actualVideo.getCastMembers());
    assertTrue(actualVideo.getVideo().isEmpty());
    assertEquals(aTrailerMedia, actualVideo.getTrailer().get());
    assertTrue(actualVideo.getBanner().isEmpty());
    assertTrue(actualVideo.getThumbnail().isEmpty());
    assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    assertEquals(expectedDomainEventSize, actualVideo.getDomainEvents().size());

    final var actualEvent = (VideoMediaCreated) actualVideo.getDomainEvents().get(0);
    assertEquals(aVideo.getId().getValue(), actualEvent.resourceId());
    assertEquals(aTrailerMedia.rawLocation(), actualEvent.filePath());
    assertNotNull(actualEvent.occurredOn());

    assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsUpdateBannerMedia_shouldReturnUpdated()
      throws InterruptedException {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription =
        """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());

    final var aVideo =
        Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers);

    final var aBannerMedia = ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

    Thread.sleep(1);

    // when
    final var actualVideo = Video.with(aVideo).updateBannerMedia(aBannerMedia);

    // then
    assertNotNull(actualVideo);
    assertNotNull(actualVideo.getId());
    assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
    assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    assertEquals(expectedTitle, actualVideo.getTitle());
    assertEquals(expectedDescription, actualVideo.getDescription());
    assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    assertEquals(expectedDuration, actualVideo.getDuration());
    assertEquals(expectedOpened, actualVideo.getOpened());
    assertEquals(expectedPublished, actualVideo.getPublished());
    assertEquals(expectedRating, actualVideo.getRating());
    assertEquals(expectedCategories, actualVideo.getCategories());
    assertEquals(expectedGenres, actualVideo.getGenres());
    assertEquals(expectedMembers, actualVideo.getCastMembers());
    assertTrue(actualVideo.getVideo().isEmpty());
    assertTrue(actualVideo.getTrailer().isEmpty());
    assertEquals(aBannerMedia, actualVideo.getBanner().get());
    assertTrue(actualVideo.getThumbnail().isEmpty());
    assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsUpdateThumbnailMedia_shouldReturnUpdated()
      throws InterruptedException {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription =
        """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());

    final var aVideo =
        Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers);

    final var aThumbMedia = ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

    Thread.sleep(1);

    // when
    final var actualVideo = Video.with(aVideo).updateThumbnailMedia(aThumbMedia);

    // then
    assertNotNull(actualVideo);
    assertNotNull(actualVideo.getId());
    assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
    assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    assertEquals(expectedTitle, actualVideo.getTitle());
    assertEquals(expectedDescription, actualVideo.getDescription());
    assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    assertEquals(expectedDuration, actualVideo.getDuration());
    assertEquals(expectedOpened, actualVideo.getOpened());
    assertEquals(expectedPublished, actualVideo.getPublished());
    assertEquals(expectedRating, actualVideo.getRating());
    assertEquals(expectedCategories, actualVideo.getCategories());
    assertEquals(expectedGenres, actualVideo.getGenres());
    assertEquals(expectedMembers, actualVideo.getCastMembers());
    assertTrue(actualVideo.getVideo().isEmpty());
    assertTrue(actualVideo.getTrailer().isEmpty());
    assertTrue(actualVideo.getBanner().isEmpty());
    assertEquals(aThumbMedia, actualVideo.getThumbnail().get());
    assertTrue(actualVideo.getThumbnailHalf().isEmpty());

    assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsUpdateThumbnailHalfMedia_shouldReturnUpdated()
      throws InterruptedException {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription =
        """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());

    final var aVideo =
        Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers);

    final var aThumbMedia = ImageMedia.with("abc", "Trailer.mp4", "/123/videos");

    Thread.sleep(1);

    // when
    final var actualVideo = Video.with(aVideo).updateThumbnailHalfMedia(aThumbMedia);

    // then
    assertNotNull(actualVideo);
    assertNotNull(actualVideo.getId());
    assertEquals(aVideo.getCreatedAt(), actualVideo.getCreatedAt());
    assertTrue(aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt()));
    assertEquals(expectedTitle, actualVideo.getTitle());
    assertEquals(expectedDescription, actualVideo.getDescription());
    assertEquals(expectedLaunchedAt, actualVideo.getLaunchedAt());
    assertEquals(expectedDuration, actualVideo.getDuration());
    assertEquals(expectedOpened, actualVideo.getOpened());
    assertEquals(expectedPublished, actualVideo.getPublished());
    assertEquals(expectedRating, actualVideo.getRating());
    assertEquals(expectedCategories, actualVideo.getCategories());
    assertEquals(expectedGenres, actualVideo.getGenres());
    assertEquals(expectedMembers, actualVideo.getCastMembers());
    assertTrue(actualVideo.getVideo().isEmpty());
    assertTrue(actualVideo.getTrailer().isEmpty());
    assertTrue(actualVideo.getBanner().isEmpty());
    assertTrue(actualVideo.getThumbnail().isEmpty());
    assertEquals(aThumbMedia, actualVideo.getThumbnailHalf().get());

    assertDoesNotThrow(() -> actualVideo.validate(new ThrowsValidationHandler()));
  }

  @Test
  public void givenValidVideo_whenCallsWith_shouldCreateWithoutEvents() {
    // given
    final var expectedTitle = "System Design Interviews";
    final var expectedDescription =
        """
                Disclaimer: o estudo de caso apresentado tem fins educacionais e representa nossas opiniões pessoais.
                Esse vídeo faz parte da Imersão Full Stack && Full Cycle.
                Para acessar todas as aulas, lives e desafios, acesse:
                https://imersao.fullcycle.com.br/
                """;
    final var expectedLaunchedAt = Year.of(2022);
    final var expectedDuration = 120.10;
    final var expectedOpened = false;
    final var expectedPublished = false;
    final var expectedRating = Rating.L;
    final var expectedCategories = Set.of(CategoryID.unique());
    final var expectedGenres = Set.of(GenreID.unique());
    final var expectedMembers = Set.of(CastMemberID.unique());

    // when
    final var actualVideo =
        Video.with(
            VideoID.unique(),
            expectedTitle,
            expectedDescription,
            expectedLaunchedAt,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            InstantUtils.now(),
            InstantUtils.now(),
            null,
            null,
            null,
            null,
            null,
            expectedCategories,
            expectedGenres,
            expectedMembers);

    // then
    assertNotNull(actualVideo.getDomainEvents());
  }
}
