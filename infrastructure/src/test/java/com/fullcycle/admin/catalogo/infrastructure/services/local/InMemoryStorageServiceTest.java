package com.fullcycle.admin.catalogo.infrastructure.services.local;

import static com.fullcycle.admin.catalogo.domain.video.VideoMediaType.THUMBNAIL;

import com.fullcycle.admin.catalogo.domain.Fixture;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InMemoryStorageAPITest {

  private InMemoryStorageService target = new InMemoryStorageService();

  @BeforeEach
  public void setUp() {
    target.clear();
  }

  @Test
  public void givenValidResource_whenCallsStore_shouldStoreIt() {
    final var expectedResource = Fixture.Videos.resource(THUMBNAIL);
    final var expectedId = "item";

    target.store(expectedId, expectedResource);

    final var actualContent = this.target.storage().get(expectedId);

    Assertions.assertEquals(expectedResource, actualContent);
  }

  @Test
  public void givenResource_whenCallsGet_shouldRetrieveIt() {
    final var expectedResource = Fixture.Videos.resource(THUMBNAIL);
    final var expectedId = "item";

    this.target.storage().put(expectedId, expectedResource);

    final var actualContent = target.get(expectedId);

    Assertions.assertEquals(expectedResource, actualContent);
  }

  @Test
  public void givenPrefix_whenCallsList_shouldRetrieveAll() {
    final var expectedResource = Fixture.Videos.resource(THUMBNAIL);

    final var expectedIds = List.of("item1", "item2");

    this.target.storage().put("item1", expectedResource);
    this.target.storage().put("item2", expectedResource);

    final var actualContent = target.list("it");

    Assertions.assertTrue(
        expectedIds.size() == actualContent.size() && expectedIds.containsAll(actualContent));
  }

  @Test
  public void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {
    final var expectedResource = Fixture.Videos.resource(THUMBNAIL);

    final var expectedIds = List.of("item1", "item2");

    this.target.storage().put("item1", expectedResource);
    this.target.storage().put("item2", expectedResource);

    target.deleteAll(expectedIds);

    Assertions.assertTrue(this.target.storage().isEmpty());
  }
}
