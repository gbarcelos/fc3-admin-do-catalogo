package com.fullcycle.admin.catalogo.application.video.create;

import static com.fullcycle.admin.catalogo.domain.Fixture.Videos.resource;
import static com.fullcycle.admin.catalogo.domain.utils.IdUtils.uuid;
import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.InternalErrorException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.resource.Resource;
import com.fullcycle.admin.catalogo.domain.video.*;
import java.time.Year;
import java.util.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class CreateVideoUseCaseTest extends UseCaseTest {

  @InjectMocks private DefaultCreateVideoUseCase useCase;

  @Mock private VideoGateway videoGateway;

  @Mock private CategoryGateway categoryGateway;

  @Mock private CastMemberGateway castMemberGateway;

  @Mock private GenreGateway genreGateway;

  @Mock private MediaResourceGateway mediaResourceGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(
        videoGateway, categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway);
  }

  @Test
  public void givenAValidCommand_whenCallsCreateVideo_shouldReturnVideoId() {
    // given
    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
    final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
    final var expectedMembers =
        Set.of(Fixture.CastMembers.wesley().getId(), Fixture.CastMembers.gabriel().getId());
    final Resource expectedVideo = resource(VIDEO);
    final Resource expectedTrailer = resource(TRAILER);
    final Resource expectedBanner = resource(BANNER);
    final Resource expectedThumb = resource(THUMBNAIL);
    final Resource expectedThumbHalf = resource(THUMBNAIL_HALF);

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway)
        .create(
            argThat(
                actualVideo ->
                    Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(
                            expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(
                            expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(
                            expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(
                            expectedThumbHalf.name(),
                            actualVideo.getThumbnailHalf().get().name())));
  }

  @Test
  public void givenAValidCommandWithoutCategories_whenCallsCreateVideo_shouldReturnVideoId() {
    // given
    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
    final var expectedMembers =
        Set.of(Fixture.CastMembers.wesley().getId(), Fixture.CastMembers.gabriel().getId());
    final Resource expectedVideo = resource(VIDEO);
    final Resource expectedTrailer = resource(TRAILER);
    final Resource expectedBanner = resource(BANNER);
    final Resource expectedThumb = resource(THUMBNAIL);
    final Resource expectedThumbHalf = resource(THUMBNAIL_HALF);

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway)
        .create(
            argThat(
                actualVideo ->
                    Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(
                            expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(
                            expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(
                            expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(
                            expectedThumbHalf.name(),
                            actualVideo.getThumbnailHalf().get().name())));
  }

  @Test
  public void givenAValidCommandWithoutGenres_whenCallsCreateVideo_shouldReturnVideoId() {
    // given
    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers =
        Set.of(Fixture.CastMembers.wesley().getId(), Fixture.CastMembers.gabriel().getId());
    final Resource expectedVideo = resource(VIDEO);
    final Resource expectedTrailer = resource(TRAILER);
    final Resource expectedBanner = resource(BANNER);
    final Resource expectedThumb = resource(THUMBNAIL);
    final Resource expectedThumbHalf = resource(THUMBNAIL_HALF);

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway)
        .create(
            argThat(
                actualVideo ->
                    Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(
                            expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(
                            expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(
                            expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(
                            expectedThumbHalf.name(),
                            actualVideo.getThumbnailHalf().get().name())));
  }

  @Test
  public void givenAValidCommandWithoutCastMembers_whenCallsCreateVideo_shouldReturnVideoId() {
    // given
    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
    final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = resource(VIDEO);
    final Resource expectedTrailer = resource(TRAILER);
    final Resource expectedBanner = resource(BANNER);
    final Resource expectedThumb = resource(THUMBNAIL);
    final Resource expectedThumbHalf = resource(THUMBNAIL_HALF);

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));

    when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway)
        .create(
            argThat(
                actualVideo ->
                    Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
                        && Objects.equals(
                            expectedTrailer.name(), actualVideo.getTrailer().get().name())
                        && Objects.equals(
                            expectedBanner.name(), actualVideo.getBanner().get().name())
                        && Objects.equals(
                            expectedThumb.name(), actualVideo.getThumbnail().get().name())
                        && Objects.equals(
                            expectedThumbHalf.name(),
                            actualVideo.getThumbnailHalf().get().name())));
  }

  @Test
  public void givenAValidCommandWithoutResources_whenCallsCreateVideo_shouldReturnVideoId() {
    // given
    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
    final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
    final var expectedMembers =
        Set.of(Fixture.CastMembers.wesley().getId(), Fixture.CastMembers.gabriel().getId());
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

    when(videoGateway.create(any())).thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway)
        .create(
            argThat(
                actualVideo ->
                    Objects.equals(expectedTitle, actualVideo.getTitle())
                        && Objects.equals(expectedDescription, actualVideo.getDescription())
                        && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
                        && Objects.equals(expectedDuration, actualVideo.getDuration())
                        && Objects.equals(expectedOpened, actualVideo.getOpened())
                        && Objects.equals(expectedPublished, actualVideo.getPublished())
                        && Objects.equals(expectedRating, actualVideo.getRating())
                        && Objects.equals(expectedCategories, actualVideo.getCategories())
                        && Objects.equals(expectedGenres, actualVideo.getGenres())
                        && Objects.equals(expectedMembers, actualVideo.getCastMembers())
                        && actualVideo.getVideo().isEmpty()
                        && actualVideo.getTrailer().isEmpty()
                        && actualVideo.getBanner().isEmpty()
                        && actualVideo.getThumbnail().isEmpty()
                        && actualVideo.getThumbnailHalf().isEmpty()));
  }

  @Test
  public void givenANullTitle_whenCallsCreateVideo_shouldReturnDomainException() {
    // given
    final var expectedErrorMessage = "'title' should not be null";
    final var expectedErrorCount = 1;

    final String expectedTitle = null;
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    // when
    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              useCase.execute(aCommand);
            });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, never()).existsByIds(any());
    verify(castMemberGateway, never()).existsByIds(any());
    verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    verify(genreGateway, never()).existsByIds(any());
    verify(videoGateway, never()).create(any());
  }

  @Test
  public void givenAEmptyTitle_whenCallsCreateVideo_shouldReturnDomainException() {
    // given
    final var expectedErrorMessage = "'title' should not be empty";
    final var expectedErrorCount = 1;

    final var expectedTitle = "";
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    // when
    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              useCase.execute(aCommand);
            });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, never()).existsByIds(any());
    verify(castMemberGateway, never()).existsByIds(any());
    verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    verify(genreGateway, never()).existsByIds(any());
    verify(videoGateway, never()).create(any());
  }

  @Test
  public void givenANullRating_whenCallsCreateVideo_shouldReturnDomainException() {
    // given
    final var expectedErrorMessage = "'rating' should not be null";
    final var expectedErrorCount = 1;

    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final String expectedRating = null;
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    // when
    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              useCase.execute(aCommand);
            });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, never()).existsByIds(any());
    verify(castMemberGateway, never()).existsByIds(any());
    verify(mediaResourceGateway, never()).storeImage(any(), any());
    verify(genreGateway, never()).existsByIds(any());
    verify(videoGateway, never()).create(any());
  }

  @Test
  public void givenAnInvalidRating_whenCallsCreateVideo_shouldReturnDomainException() {
    // given
    final var expectedErrorMessage = "'rating' should not be null";
    final var expectedErrorCount = 1;

    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = "JAIAJIA";
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    // when
    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              useCase.execute(aCommand);
            });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, never()).existsByIds(any());
    verify(castMemberGateway, never()).existsByIds(any());
    verify(mediaResourceGateway, never()).storeAudioVideo(any(), any());
    verify(genreGateway, never()).existsByIds(any());
    verify(videoGateway, never()).create(any());
  }

  @Test
  public void givenANullLaunchYear_whenCallsCreateVideo_shouldReturnDomainException() {
    // given
    final var expectedErrorMessage = "'launchedAt' should not be null";
    final var expectedErrorCount = 1;

    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final Integer expectedLaunchYear = null;
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    // when
    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              useCase.execute(aCommand);
            });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, never()).existsByIds(any());
    verify(castMemberGateway, never()).existsByIds(any());
    verify(mediaResourceGateway, never()).storeImage(any(), any());
    verify(genreGateway, never()).existsByIds(any());
    verify(videoGateway, never()).create(any());
  }

  @Test
  public void
      givenAValidCommand_whenCallsCreateVideoAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
    // given
    final var aulasId = Fixture.Categories.aulas().getId();

    final var expectedErrorMessage =
        "Some categories could not be found: %s".formatted(aulasId.getValue());
    final var expectedErrorCount = 1;

    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Fixture.year();
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.of(aulasId);
    final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
    final var expectedMembers = Set.of(Fixture.CastMembers.wesley().getId());
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>());

    when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

    // when
    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              useCase.execute(aCommand);
            });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
    verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
    verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
    verify(mediaResourceGateway, never()).storeImage(any(), any());
    verify(videoGateway, never()).create(any());
  }

  @Test
  public void
      givenAValidCommand_whenCallsCreateVideoAndSomeGenresDoesNotExists_shouldReturnDomainException() {
    // given
    final var techId = Fixture.Genres.tech().getId();

    final var expectedErrorMessage =
        "Some genres could not be found: %s".formatted(techId.getValue());
    final var expectedErrorCount = 1;

    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Fixture.year();
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
    final var expectedGenres = Set.of(techId);
    final var expectedMembers = Set.of(Fixture.CastMembers.wesley().getId());
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>());

    // when
    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              useCase.execute(aCommand);
            });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
    verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
    verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
    verify(mediaResourceGateway, never()).storeImage(any(), any());
    verify(videoGateway, never()).create(any());
  }

  @Test
  public void
      givenAValidCommand_whenCallsCreateVideoAndSomeCastMembersDoesNotExists_shouldReturnDomainException() {
    // given
    final var wesleyId = Fixture.CastMembers.wesley().getId();

    final var expectedErrorMessage =
        "Some cast members could not be found: %s".formatted(wesleyId.getValue());
    final var expectedErrorCount = 1;

    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Fixture.year();
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
    final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
    final var expectedMembers = Set.of(wesleyId);
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>());

    when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

    // when
    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              useCase.execute(aCommand);
            });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
    verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
    verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
    verify(mediaResourceGateway, never()).storeImage(any(), any());
    verify(videoGateway, never()).create(any());
  }

  @Test
  public void givenAValidCommand_whenCallsCreateVideoThrowsException_shouldCallClearResources() {
    // given
    final var expectedErrorMessage = "An error on create video was observed [videoId:";

    final var expectedTitle = Fixture.title();
    final var expectedDescription = Fixture.Videos.description();
    final var expectedLaunchYear = Year.of(Fixture.year());
    final var expectedDuration = Fixture.duration();
    final var expectedOpened = Fixture.bool();
    final var expectedPublished = Fixture.bool();
    final var expectedRating = Fixture.Videos.rating();
    final var expectedCategories = Set.of(Fixture.Categories.aulas().getId());
    final var expectedGenres = Set.of(Fixture.Genres.tech().getId());
    final var expectedMembers =
        Set.of(Fixture.CastMembers.wesley().getId(), Fixture.CastMembers.gabriel().getId());
    final Resource expectedVideo = resource(VIDEO);
    final Resource expectedTrailer = resource(TRAILER);
    final Resource expectedBanner = resource(BANNER);
    final Resource expectedThumb = resource(THUMBNAIL);
    final Resource expectedThumbHalf = resource(THUMBNAIL_HALF);

    final var aCommand =
        CreateVideoCommand.with(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear.getValue(),
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating.getName(),
            asString(expectedCategories),
            asString(expectedGenres),
            asString(expectedMembers),
            expectedVideo,
            expectedTrailer,
            expectedBanner,
            expectedThumb,
            expectedThumbHalf);

    when(categoryGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any())).thenReturn(new ArrayList<>(expectedGenres));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.create(any())).thenThrow(new RuntimeException("Internal Server Error"));

    // when
    final var actualResult =
        Assertions.assertThrows(
            InternalErrorException.class,
            () -> {
              useCase.execute(aCommand);
            });

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertTrue(actualResult.getMessage().startsWith(expectedErrorMessage));

    verify(mediaResourceGateway).clearResources(any());
  }

  private void mockImageMedia() {
    when(mediaResourceGateway.storeImage(any(), any()))
        .thenAnswer(
            t -> {
              final var resource = t.getArgument(1, Resource.class);
              return ImageMedia.with(uuid(), resource.name(), "/img");
            });
  }

  private void mockAudioVideoMedia() {
    when(mediaResourceGateway.storeAudioVideo(any(), any()))
        .thenAnswer(
            t -> {
              final var resource = t.getArgument(1, Resource.class);
              return AudioVideoMedia.with(
                  uuid(), uuid(), resource.name(), "/img", "", MediaStatus.PENDING);
            });
  }
}
