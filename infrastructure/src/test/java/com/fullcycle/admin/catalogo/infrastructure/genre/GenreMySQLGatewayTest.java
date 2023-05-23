package com.fullcycle.admin.catalogo.infrastructure.genre;

import com.fullcycle.admin.catalogo.MySQLGatewayTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.infrastructure.category.CategoryMySQLGateway;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

  @Autowired private CategoryMySQLGateway categoryGateway;

  @Autowired private GenreMySQLGateway genreGateway;

  @Autowired private GenreRepository genreRepository;

  @Test
  public void testDependenciesInjected() {
    Assertions.assertNotNull(categoryGateway);
    Assertions.assertNotNull(genreGateway);
    Assertions.assertNotNull(genreRepository);
  }

  @Test
  public void givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre() {
    // given
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId());

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
    aGenre.addCategories(expectedCategories);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    // when
    final var actualGenre = genreGateway.create(aGenre);

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
    Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    // when
    final var actualGenre = genreGateway.create(aGenre);

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), persistedGenre.getUpdatedAt());
    Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void
      givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {
    // given
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
    final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId(), series.getId());

    final var aGenre = Genre.newGenre("ac", expectedIsActive);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals("ac", aGenre.getName());
    Assertions.assertEquals(0, aGenre.getCategories().size());

    // when
    final var actualGenre =
        genreGateway.update(
            Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertIterableEquals(
        sorted(expectedCategories), sorted(actualGenre.getCategories()));
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertIterableEquals(
        sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()));
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void
      givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {
    // given
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
    final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre("ac", expectedIsActive);
    aGenre.addCategories(List.of(filmes.getId(), series.getId()));

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals("ac", aGenre.getName());
    Assertions.assertEquals(2, aGenre.getCategories().size());

    // when
    final var actualGenre =
        genreGateway.update(
            Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertEquals(aGenre.getDeletedAt(), persistedGenre.getDeletedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre(expectedName, false);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertFalse(aGenre.isActive());
    Assertions.assertNotNull(aGenre.getDeletedAt());

    // when
    final var actualGenre =
        genreGateway.update(
            Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre(expectedName, true);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertTrue(aGenre.isActive());
    Assertions.assertNull(aGenre.getDeletedAt());

    // when
    final var actualGenre =
        genreGateway.update(
            Genre.with(aGenre).update(expectedName, expectedIsActive, expectedCategories));

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNotNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertNotNull(persistedGenre.getDeletedAt());
  }

  @Test
  public void givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {
    // given
    final var aGenre = Genre.newGenre("Ação", true);

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals(1, genreRepository.count());

    // when
    genreGateway.deleteById(aGenre.getId());

    // then
    Assertions.assertEquals(0, genreRepository.count());
  }

  @Test
  public void givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOK() {
    // given
    Assertions.assertEquals(0, genreRepository.count());

    // when
    genreGateway.deleteById(GenreID.from("123"));

    // then
    Assertions.assertEquals(0, genreRepository.count());
  }

  @Test
  public void givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre() {
    // given
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
    final var series = categoryGateway.create(Category.newCategory("Séries", null, true));

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId(), series.getId());

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
    aGenre.addCategories(expectedCategories);

    final var expectedId = aGenre.getId();

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals(1, genreRepository.count());

    // when
    final var actualGenre = genreGateway.findById(expectedId).get();

    // then
    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAInvalidGenreId_whenCallsFindById_shouldReturnEmpty() {
    // given
    final var expectedId = GenreID.from("123");

    Assertions.assertEquals(0, genreRepository.count());

    // when
    final var actualGenre = genreGateway.findById(expectedId);

    // then
    Assertions.assertTrue(actualGenre.isEmpty());
  }

  private List<CategoryID> sorted(final List<CategoryID> expectedCategories) {
    return expectedCategories.stream().sorted(Comparator.comparing(CategoryID::getValue)).toList();
  }
}
