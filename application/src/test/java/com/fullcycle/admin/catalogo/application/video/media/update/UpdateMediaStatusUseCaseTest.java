package com.fullcycle.admin.catalogo.application.video.media.update;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.fullcycle.admin.catalogo.application.UseCaseTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.video.MediaStatus;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;
import com.fullcycle.admin.catalogo.domain.video.VideoMediaType;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;

public class UpdateMediaStatusUseCaseTest extends UseCaseTest {

  @InjectMocks private DefaultUpdateMediaStatusUseCase useCase;

  @Mock private VideoGateway videoGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(videoGateway);
  }

  @Test
  public void givenCommandForVideo_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
    // given
    final var expectedStatus = MediaStatus.COMPLETED;
    final var expectedFolder = "encoded_media";
    final var expectedFilename = "filename.mp4";
    final var expectedType = VideoMediaType.VIDEO;
    final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

    final var aVideo = Fixture.Videos.systemDesign().updateVideoMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));

    when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

    final var aCmd =
        UpdateMediaStatusCommand.with(
            expectedStatus,
            expectedId.getValue(),
            expectedMedia.id(),
            expectedFolder,
            expectedFilename);

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(1)).findById(eq(expectedId));

    final var captor = ArgumentCaptor.forClass(Video.class);

    verify(videoGateway, times(1)).update(captor.capture());

    final var actualVideo = captor.getValue();

    assertTrue(actualVideo.getTrailer().isEmpty());

    final var actualVideoMedia = actualVideo.getVideo().get();

    assertEquals(expectedMedia.id(), actualVideoMedia.id());
    assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
    assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
    assertEquals(expectedStatus, actualVideoMedia.status());
    assertEquals(
        expectedFolder.concat("/").concat(expectedFilename), actualVideoMedia.encodedLocation());
  }

  @Test
  public void givenCommandForVideo_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
    // given
    final var expectedStatus = MediaStatus.PROCESSING;
    final String expectedFolder = null;
    final String expectedFilename = null;
    final var expectedType = VideoMediaType.VIDEO;
    final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

    final var aVideo = Fixture.Videos.systemDesign().updateVideoMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));

    when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

    final var aCmd =
        UpdateMediaStatusCommand.with(
            expectedStatus,
            expectedId.getValue(),
            expectedMedia.id(),
            expectedFolder,
            expectedFilename);

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(1)).findById(eq(expectedId));

    final var captor = ArgumentCaptor.forClass(Video.class);

    verify(videoGateway, times(1)).update(captor.capture());

    final var actualVideo = captor.getValue();

    assertTrue(actualVideo.getTrailer().isEmpty());

    final var actualVideoMedia = actualVideo.getVideo().get();

    assertEquals(expectedMedia.id(), actualVideoMedia.id());
    assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
    assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
    assertEquals(expectedStatus, actualVideoMedia.status());
    assertTrue(actualVideoMedia.encodedLocation().isBlank());
  }

  @Test
  public void givenCommandForTrailer_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
    // given
    final var expectedStatus = MediaStatus.COMPLETED;
    final var expectedFolder = "encoded_media";
    final var expectedFilename = "filename.mp4";
    final var expectedType = VideoMediaType.TRAILER;
    final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

    final var aVideo = Fixture.Videos.systemDesign().updateTrailerMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));

    when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

    final var aCmd =
        UpdateMediaStatusCommand.with(
            expectedStatus,
            expectedId.getValue(),
            expectedMedia.id(),
            expectedFolder,
            expectedFilename);

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(1)).findById(eq(expectedId));

    final var captor = ArgumentCaptor.forClass(Video.class);

    verify(videoGateway, times(1)).update(captor.capture());

    final var actualVideo = captor.getValue();

    assertTrue(actualVideo.getVideo().isEmpty());

    final var actualVideoMedia = actualVideo.getTrailer().get();

    assertEquals(expectedMedia.id(), actualVideoMedia.id());
    assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
    assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
    assertEquals(expectedStatus, actualVideoMedia.status());
    assertEquals(
        expectedFolder.concat("/").concat(expectedFilename), actualVideoMedia.encodedLocation());
  }

  @Test
  public void
      givenCommandForTrailer_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
    // given
    final var expectedStatus = MediaStatus.PROCESSING;
    final String expectedFolder = null;
    final String expectedFilename = null;
    final var expectedType = VideoMediaType.TRAILER;
    final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

    final var aVideo = Fixture.Videos.systemDesign().updateTrailerMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));

    when(videoGateway.update(any())).thenAnswer(returnsFirstArg());

    final var aCmd =
        UpdateMediaStatusCommand.with(
            expectedStatus,
            expectedId.getValue(),
            expectedMedia.id(),
            expectedFolder,
            expectedFilename);

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(1)).findById(eq(expectedId));

    final var captor = ArgumentCaptor.forClass(Video.class);

    verify(videoGateway, times(1)).update(captor.capture());

    final var actualVideo = captor.getValue();

    assertTrue(actualVideo.getVideo().isEmpty());

    final var actualVideoMedia = actualVideo.getTrailer().get();

    assertEquals(expectedMedia.id(), actualVideoMedia.id());
    assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
    assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
    assertEquals(expectedStatus, actualVideoMedia.status());
    assertTrue(actualVideoMedia.encodedLocation().isBlank());
  }

  @Test
  public void givenCommandForTrailer_whenIsInvalid_shouldDoNothing() {
    // given
    final var expectedStatus = MediaStatus.COMPLETED;
    final var expectedFolder = "encoded_media";
    final var expectedFilename = "filename.mp4";
    final var expectedType = VideoMediaType.TRAILER;
    final var expectedMedia = Fixture.Videos.audioVideo(expectedType);

    final var aVideo = Fixture.Videos.systemDesign().updateTrailerMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any())).thenReturn(Optional.of(aVideo));

    final var aCmd =
        UpdateMediaStatusCommand.with(
            expectedStatus, expectedId.getValue(), "randomId", expectedFolder, expectedFilename);

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(0)).update(any());
  }
}
