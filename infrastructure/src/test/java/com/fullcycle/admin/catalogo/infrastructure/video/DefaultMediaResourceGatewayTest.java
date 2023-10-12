package com.fullcycle.admin.catalogo.infrastructure.video;

import static com.fullcycle.admin.catalogo.domain.Fixture.Videos.mediaType;
import static com.fullcycle.admin.catalogo.domain.Fixture.Videos.resource;
import static com.fullcycle.admin.catalogo.domain.video.MediaStatus.PENDING;
import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.video.*;
import com.fullcycle.admin.catalogo.infrastructure.services.StorageService;
import com.fullcycle.admin.catalogo.infrastructure.services.local.InMemoryStorageService;
import java.util.ArrayList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
class DefaultMediaResourceGatewayTest {

  @Autowired private MediaResourceGateway mediaResourceGateway;

  @Autowired private StorageService storageService;

  @BeforeEach
  public void setUp() {
    storageService().clear();
  }

  @Test
  public void testInjection() {
    Assertions.assertNotNull(mediaResourceGateway);
    Assertions.assertInstanceOf(DefaultMediaResourceGateway.class, mediaResourceGateway);

    Assertions.assertNotNull(storageService);
    Assertions.assertInstanceOf(InMemoryStorageService.class, storageService);
  }

  @Test
  public void givenValidResource_whenCallsStorageAudioVideo_shouldStoreIt() {
    // given
    final var expectedVideoId = VideoID.unique();
    final var expectedType = VIDEO;
    final var expectedResource = resource(expectedType);
    final var expectedLocation =
        "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());
    final var expectedStatus = PENDING;
    final var expectedEncodedLocation = "";

    // when
    final var actualMedia =
        this.mediaResourceGateway.storeAudioVideo(
            expectedVideoId, VideoResource.with(expectedType, expectedResource));

    // then
    Assertions.assertNotNull(actualMedia.id());
    assertEquals(expectedLocation, actualMedia.rawLocation());
    assertEquals(expectedResource.name(), actualMedia.name());
    assertEquals(expectedResource.checksum(), actualMedia.checksum());
    assertEquals(expectedStatus, actualMedia.status());
    assertEquals(expectedEncodedLocation, actualMedia.encodedLocation());

    final var actualStored = storageService().storage().get(expectedLocation);

    assertEquals(expectedResource, actualStored);
  }

  @Test
  public void givenValidResource_whenCallsStorageImage_shouldStoreIt() {
    // given
    final var expectedVideoId = VideoID.unique();
    final var expectedType = BANNER;
    final var expectedResource = resource(expectedType);
    final var expectedLocation =
        "videoId-%s/type-%s".formatted(expectedVideoId.getValue(), expectedType.name());

    // when
    final var actualMedia =
        this.mediaResourceGateway.storeImage(
            expectedVideoId, VideoResource.with(expectedType, expectedResource));

    // then
    Assertions.assertNotNull(actualMedia.id());
    assertEquals(expectedLocation, actualMedia.location());
    assertEquals(expectedResource.name(), actualMedia.name());
    assertEquals(expectedResource.checksum(), actualMedia.checksum());

    final var actualStored = storageService().storage().get(expectedLocation);

    assertEquals(expectedResource, actualStored);
  }

  @Test
  public void givenValidVideoId_whenCallsGetResource_shouldReturnIt() {
    // given
    final var videoOne = VideoID.unique();
    final var expectedType = VIDEO;
    final var expectedResource = resource(expectedType);

    storageService()
        .store("videoId-%s/type-%s".formatted(videoOne.getValue(), expectedType), expectedResource);
    storageService()
        .store(
            "videoId-%s/type-%s".formatted(videoOne.getValue(), TRAILER.name()),
            resource(mediaType()));
    storageService()
        .store(
            "videoId-%s/type-%s".formatted(videoOne.getValue(), BANNER.name()),
            resource(mediaType()));

    assertEquals(3, storageService().storage().size());

    // when
    final var actualResult = this.mediaResourceGateway.getResource(videoOne, expectedType).get();

    // then
    assertEquals(expectedResource, actualResult);
  }

  @Test
  public void givenInvalidType_whenCallsGetResource_shouldReturnEmpty() {
    // given
    final var videoOne = VideoID.unique();
    final var expectedType = THUMBNAIL;

    storageService()
        .store(
            "videoId-%s/type-%s".formatted(videoOne.getValue(), VIDEO.name()),
            resource(mediaType()));
    storageService()
        .store(
            "videoId-%s/type-%s".formatted(videoOne.getValue(), TRAILER.name()),
            resource(mediaType()));
    storageService()
        .store(
            "videoId-%s/type-%s".formatted(videoOne.getValue(), BANNER.name()),
            resource(mediaType()));

    assertEquals(3, storageService().storage().size());

    // when
    final var actualResult = this.mediaResourceGateway.getResource(videoOne, expectedType);

    // then
    assertTrue(actualResult.isEmpty());
  }

  @Test
  public void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
    // given
    final var videoOne = VideoID.unique();
    final var videoTwo = VideoID.unique();

    final var toBeDeleted = new ArrayList<String>();
    toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), VIDEO.name()));
    toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), TRAILER.name()));
    toBeDeleted.add("videoId-%s/type-%s".formatted(videoOne.getValue(), BANNER.name()));

    final var expectedValues = new ArrayList<String>();
    expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), VIDEO.name()));
    expectedValues.add("videoId-%s/type-%s".formatted(videoTwo.getValue(), BANNER.name()));

    toBeDeleted.forEach(id -> storageService().store(id, resource(mediaType())));
    expectedValues.forEach(id -> storageService().store(id, resource(mediaType())));

    assertEquals(5, storageService().storage().size());

    // when
    this.mediaResourceGateway.clearResources(videoOne);

    // then
    assertEquals(2, storageService().storage().size());

    final var actualKeys = storageService().storage().keySet();

    assertTrue(
        expectedValues.size() == actualKeys.size() && actualKeys.containsAll(expectedValues));
  }

  private InMemoryStorageService storageService() {
    return (InMemoryStorageService) storageService;
  }
}
