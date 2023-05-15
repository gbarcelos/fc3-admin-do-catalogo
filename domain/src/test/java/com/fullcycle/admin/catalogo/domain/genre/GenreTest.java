package com.fullcycle.admin.catalogo.domain.genre;

import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GenreTest {

  @Test
  public void givenValidParams_whenCallNewGenre_shouldInstantiateAGenre() {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = 0;

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError() {
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenInvalidEmptyName_whenCallNewGenreAndValidate_shouldReceiveAError() {
    final var expectedName = " ";
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be empty";

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void
      givenInvalidNameWithLengthGreaterThan255_whenCallNewGenreAndValidate_shouldReceiveAError() {
    final var expectedName =
        """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla posuere nisl facilisis mi sagittis efficitur. Sed ultrices hendrerit odio, non aliquet sapien commodo vel. Maecenas faucibus varius orci, ac vulputate arcu finibus sed. Vestibulum sollicitudin metus in dictum suscipit. Phasellus lacinia nunc mauris, non scelerisque orci luctus id. Pellentesque eget leo imperdiet, auctor purus vehicula, cursus mi. Sed ut ipsum enim. Nam nisl lectus, lobortis ac sagittis ac, venenatis et lorem. Donec dictum cursus arcu at cursus. Phasellus sed est ac sapien faucibus posuere. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Sed in dolor vitae justo tincidunt dignissim sed quis tellus.
                Ut egestas ex ut nibh volutpat, id ullamcorper massa molestie. Proin tincidunt enim nec est vulputate fringilla. Maecenas commodo nibh eget mauris suscipit tempus. Proin gravida pretium felis, sit amet fermentum urna aliquam vitae. Proin et arcu dui. Sed maximus sed nulla in volutpat. Quisque varius ac ex sed molestie. Ut auctor varius dapibus. Fusce consectetur quam et sagittis semper. Morbi iaculis nisl non turpis mollis venenatis. Pellentesque non facilisis sem, quis semper lacus. Maecenas et maximus tellus, non hendrerit enim. Duis mattis ipsum eget consectetur mattis.
                Vestibulum venenatis aliquam nunc, ac elementum ante tristique a. Donec vel porta nibh, at dignissim lorem. Vivamus tincidunt lacinia mauris, ac scelerisque enim laoreet eget. Sed semper dictum pharetra. Donec condimentum accumsan augue, vel imperdiet enim pulvinar at. In eleifend, lacus eget posuere imperdiet, urna risus auctor enim, vitae sagittis turpis justo id.\s
                """;
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> Genre.newGenre(expectedName, expectedIsActive));

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAnActiveGenre_whenCallDeactivate_shouldReceiveOK() throws InterruptedException {
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = 0;

    final var actualGenre = Genre.newGenre(expectedName, true);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertTrue(actualGenre.isActive());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1);
    actualGenre.deactivate();

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNotNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAnInactiveGenre_whenCallActivate_shouldReceiveOK() throws InterruptedException {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = 0;

    final var actualGenre = Genre.newGenre(expectedName, false);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertFalse(actualGenre.isActive());
    Assertions.assertNotNull(actualGenre.getDeletedAt());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1);
    actualGenre.activate();

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAValidInactiveGenre_whenCallUpdateWithActivate_shouldReceiveGenreUpdated()
      throws InterruptedException {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(CategoryID.from("123"));

    final var actualGenre = Genre.newGenre("acao", false);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertFalse(actualGenre.isActive());
    Assertions.assertNotNull(actualGenre.getDeletedAt());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1);
    actualGenre.update(expectedName, expectedIsActive, expectedCategories);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated()
      throws InterruptedException {
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.of(CategoryID.from("123"));

    final var actualGenre = Genre.newGenre("acao", true);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertTrue(actualGenre.isActive());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1);
    actualGenre.update(expectedName, expectedIsActive, expectedCategories);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNotNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException() {
    final var expectedName = " ";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(CategoryID.from("123"));
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be empty";

    final var actualGenre = Genre.newGenre("acao", false);

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              actualGenre.update(expectedName, expectedIsActive, expectedCategories);
            });

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAValidGenre_whenCallUpdateWithNullName_shouldReceiveNotificationException() {
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedCategories = List.of(CategoryID.from("123"));
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var actualGenre = Genre.newGenre("acao", false);

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class,
            () -> {
              actualGenre.update(expectedName, expectedIsActive, expectedCategories);
            });

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK()
      throws InterruptedException {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = new ArrayList<CategoryID>();

    final var actualGenre = Genre.newGenre("acao", expectedIsActive);

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1);
    Assertions.assertDoesNotThrow(
        () -> {
          actualGenre.update(expectedName, expectedIsActive, null);
        });

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOK()
      throws InterruptedException {
    final var seriesID = CategoryID.from("123");
    final var moviesID = CategoryID.from("456");

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(seriesID, moviesID);

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Assertions.assertEquals(0, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1);
    actualGenre.addCategory(seriesID);
    actualGenre.addCategory(moviesID);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAInvalidNullAsCategoryID_whenCallAddCategory_shouldReceiveOK()
      throws InterruptedException {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = new ArrayList<CategoryID>();

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Assertions.assertEquals(0, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1);
    actualGenre.addCategory(null);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOK()
      throws InterruptedException {
    final var seriesID = CategoryID.from("123");
    final var moviesID = CategoryID.from("456");

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(moviesID);

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Thread.sleep(1);
    actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

    Assertions.assertEquals(2, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1);
    actualGenre.removeCategory(seriesID);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  public void givenAnInvalidNullAsCategoryID_whenCallRemoveCategory_shouldReceiveOK()
      throws InterruptedException {
    final var seriesID = CategoryID.from("123");
    final var moviesID = CategoryID.from("456");

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(seriesID, moviesID);

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Thread.sleep(1);
    actualGenre.update(expectedName, expectedIsActive, expectedCategories);

    Assertions.assertEquals(2, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    Thread.sleep(1);
    actualGenre.removeCategory(null);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }
}
