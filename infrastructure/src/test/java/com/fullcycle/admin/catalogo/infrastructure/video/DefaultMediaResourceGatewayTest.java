package com.fullcycle.admin.catalogo.infrastructure.video;

import static com.fullcycle.admin.catalogo.domain.Fixture.Videos.mediaType;
import static com.fullcycle.admin.catalogo.domain.Fixture.Videos.resource;
import static com.fullcycle.admin.catalogo.domain.video.MediaStatus.PENDING;
import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.*;

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
    Assertions.assertEquals(expectedLocation, actualMedia.rawLocation());
    Assertions.assertEquals(expectedResource.name(), actualMedia.name());
    Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());
    Assertions.assertEquals(expectedStatus, actualMedia.status());
    Assertions.assertEquals(expectedEncodedLocation, actualMedia.encodedLocation());

    final var actualStored = storageService().storage().get(expectedLocation);

    Assertions.assertEquals(expectedResource, actualStored);
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
    Assertions.assertEquals(expectedLocation, actualMedia.location());
    Assertions.assertEquals(expectedResource.name(), actualMedia.name());
    Assertions.assertEquals(expectedResource.checksum(), actualMedia.checksum());

    final var actualStored = storageService().storage().get(expectedLocation);

    Assertions.assertEquals(expectedResource, actualStored);
  }

  @Test
  public void givenValidVideoId_whenCallsClearResources_shouldDeleteAll() {
    // given
    final var videoOne = VideoID.unique();
    final var videoTwo = VideoID.unique();

    final var toBeDeleted = new ArrayList<String>();
    toBeDeleted.add(
        "videoId-%s/type-%s".formatted(videoOne.getValue(), VIDEO.name()));
    toBeDeleted.add(
        "videoId-%s/type-%s".formatted(videoOne.getValue(), TRAILER.name()));
    toBeDeleted.add(
        "videoId-%s/type-%s".formatted(videoOne.getValue(), BANNER.name()));

    final var expectedValues = new ArrayList<String>();
    expectedValues.add(
        "videoId-%s/type-%s".formatted(videoTwo.getValue(), VIDEO.name()));
    expectedValues.add(
        "videoId-%s/type-%s".formatted(videoTwo.getValue(), BANNER.name()));

    toBeDeleted.forEach(id -> storageService().store(id, resource(mediaType())));
    expectedValues.forEach(id -> storageService().store(id, resource(mediaType())));

    Assertions.assertEquals(5, storageService().storage().size());

    // when
    this.mediaResourceGateway.clearResources(videoOne);

    // then
    Assertions.assertEquals(2, storageService().storage().size());

    final var actualKeys = storageService().storage().keySet();

    Assertions.assertTrue(
        expectedValues.size() == actualKeys.size() && actualKeys.containsAll(expectedValues));
  }

  private InMemoryStorageService storageService() {
    return (InMemoryStorageService) storageService;
  }
}
