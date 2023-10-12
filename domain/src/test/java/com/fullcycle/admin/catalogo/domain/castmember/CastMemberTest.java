package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.UnitTest;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CastMemberTest extends UnitTest {

  @Test
  public void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
    final var expectedName = "Vin Diesel";
    final var expectedType = CastMemberType.ACTOR;

    final var actualMember = CastMember.newMember(expectedName, expectedType);

    Assertions.assertNotNull(actualMember);
    Assertions.assertNotNull(actualMember.getId());
    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertNotNull(actualMember.getCreatedAt());
    Assertions.assertNotNull(actualMember.getUpdatedAt());
    Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
  }

  @Test
  public void givenAInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
    final String expectedName = null;
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
    final var expectedName = " ";
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be empty";

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void
      givenAInvalidNameWithLengthMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
    final var expectedName =
        """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla posuere nisl facilisis mi sagittis efficitur. Sed ultrices hendrerit odio, non aliquet sapien commodo vel. Maecenas faucibus varius orci, ac vulputate arcu finibus sed. Vestibulum sollicitudin metus in dictum suscipit. Phasellus lacinia nunc mauris, non scelerisque orci luctus id. Pellentesque eget leo imperdiet, auctor purus vehicula, cursus mi. Sed ut ipsum enim. Nam nisl lectus, lobortis ac sagittis ac, venenatis et lorem. Donec dictum cursus arcu at cursus. Phasellus sed est ac sapien faucibus posuere. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Sed in dolor vitae justo tincidunt dignissim sed quis tellus.
                Ut egestas ex ut nibh volutpat, id ullamcorper massa molestie. Proin tincidunt enim nec est vulputate fringilla. Maecenas commodo nibh eget mauris suscipit tempus. Proin gravida pretium felis, sit amet fermentum urna aliquam vitae. Proin et arcu dui. Sed maximus sed nulla in volutpat. Quisque varius ac ex sed molestie. Ut auctor varius dapibus. Fusce consectetur quam et sagittis semper. Morbi iaculis nisl non turpis mollis venenatis. Pellentesque non facilisis sem, quis semper lacus. Maecenas et maximus tellus, non hendrerit enim. Duis mattis ipsum eget consectetur mattis.
                Vestibulum venenatis aliquam nunc, ac elementum ante tristique a. Donec vel porta nibh, at dignissim lorem. Vivamus tincidunt lacinia mauris, ac scelerisque enim laoreet eget. Sed semper dictum pharetra. Donec condimentum accumsan augue, vel imperdiet enim pulvinar at. In eleifend, lacus eget posuere imperdiet, urna risus auctor enim, vitae sagittis turpis justo id.\s
                """;
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAInvalidNullType_whenCallsNewMember_shouldReceiveANotification() {
    final var expectedName = "Vin Diesel";
    final CastMemberType expectedType = null;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'type' should not be null";

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> CastMember.newMember(expectedName, expectedType));

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() throws InterruptedException {
    final var expectedName = "Vin Diesel";
    final var expectedType = CastMemberType.ACTOR;

    final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(actualMember);
    Assertions.assertNotNull(actualMember.getId());

    final var actualID = actualMember.getId();
    final var actualCreatedAt = actualMember.getCreatedAt();
    final var actualUpdatedAt = actualMember.getUpdatedAt();

    Thread.sleep(1);
    actualMember.update(expectedName, expectedType);

    Assertions.assertEquals(actualID, actualMember.getId());
    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertEquals(actualCreatedAt, actualMember.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(actualMember.getUpdatedAt()));
  }

  @Test
  public void givenAValidCastMember_whenCallUpdateWithInvalidNullName_shouldReceiveNotification() {
    final String expectedName = null;
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(actualMember);
    Assertions.assertNotNull(actualMember.getId());

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> actualMember.update(expectedName, expectedType));

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification() {
    final var expectedName = " ";
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be empty";

    final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(actualMember);
    Assertions.assertNotNull(actualMember.getId());

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> actualMember.update(expectedName, expectedType));

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void
      givenAValidCastMember_whenCallUpdateWithLengthMoreThan255_shouldReceiveNotification() {
    final var expectedName =
        """
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Nulla posuere nisl facilisis mi sagittis efficitur. Sed ultrices hendrerit odio, non aliquet sapien commodo vel. Maecenas faucibus varius orci, ac vulputate arcu finibus sed. Vestibulum sollicitudin metus in dictum suscipit. Phasellus lacinia nunc mauris, non scelerisque orci luctus id. Pellentesque eget leo imperdiet, auctor purus vehicula, cursus mi. Sed ut ipsum enim. Nam nisl lectus, lobortis ac sagittis ac, venenatis et lorem. Donec dictum cursus arcu at cursus. Phasellus sed est ac sapien faucibus posuere. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Sed in dolor vitae justo tincidunt dignissim sed quis tellus.
                Ut egestas ex ut nibh volutpat, id ullamcorper massa molestie. Proin tincidunt enim nec est vulputate fringilla. Maecenas commodo nibh eget mauris suscipit tempus. Proin gravida pretium felis, sit amet fermentum urna aliquam vitae. Proin et arcu dui. Sed maximus sed nulla in volutpat. Quisque varius ac ex sed molestie. Ut auctor varius dapibus. Fusce consectetur quam et sagittis semper. Morbi iaculis nisl non turpis mollis venenatis. Pellentesque non facilisis sem, quis semper lacus. Maecenas et maximus tellus, non hendrerit enim. Duis mattis ipsum eget consectetur mattis.
                Vestibulum venenatis aliquam nunc, ac elementum ante tristique a. Donec vel porta nibh, at dignissim lorem. Vivamus tincidunt lacinia mauris, ac scelerisque enim laoreet eget. Sed semper dictum pharetra. Donec condimentum accumsan augue, vel imperdiet enim pulvinar at. In eleifend, lacus eget posuere imperdiet, urna risus auctor enim, vitae sagittis turpis justo id.\s
                """;
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

    final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(actualMember);
    Assertions.assertNotNull(actualMember.getId());

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> actualMember.update(expectedName, expectedType));

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  public void givenAValidCastMember_whenCallUpdateWithInvalidNullType_shouldReceiveNotification() {
    final var expectedName = "Vin Diesel";
    final CastMemberType expectedType = null;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'type' should not be null";

    final var actualMember = CastMember.newMember("vind", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(actualMember);
    Assertions.assertNotNull(actualMember.getId());

    final var actualException =
        Assertions.assertThrows(
            NotificationException.class, () -> actualMember.update(expectedName, expectedType));

    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }
}
