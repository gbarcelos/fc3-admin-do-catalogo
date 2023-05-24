package com.fullcycle.admin.catalogo.application.genre.create;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class CreateGenreUseCaseIT {

  @Autowired private CreateGenreUseCase useCase;

  @SpyBean private CategoryGateway categoryGateway;

  @SpyBean private GenreGateway genreGateway;

  @Autowired private GenreRepository genreRepository;

  @Test
  public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {
    // given
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId());

    final var aCommand =
        CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualGenre = genreRepository.findById(actualOutput.id()).get();

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertTrue(
        expectedCategories.size() == actualGenre.getCategoryIDs().size()
            && expectedCategories.containsAll(actualGenre.getCategoryIDs()));
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  private List<String> asString(final List<CategoryID> categories) {
    return categories.stream().map(CategoryID::getValue).toList();
  }
}
