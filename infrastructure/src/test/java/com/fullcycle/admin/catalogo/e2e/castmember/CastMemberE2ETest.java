package com.fullcycle.admin.catalogo.e2e.castmember;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fullcycle.admin.catalogo.E2ETest;
import com.fullcycle.admin.catalogo.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberType;
import com.fullcycle.admin.catalogo.e2e.MockDsl;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@E2ETest
@Testcontainers
public class CastMemberE2ETest implements MockDsl {

  @Autowired private MockMvc mvc;

  @Autowired private CastMemberRepository castMemberRepository;

  @Container
  private static final MySQLContainer MYSQL_CONTAINER =
      new MySQLContainer("mysql:latest")
          .withPassword("123456")
          .withUsername("root")
          .withDatabaseName("adm_videos");

  @DynamicPropertySource
  public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
    registry.add("mysql.port", () -> MYSQL_CONTAINER.getMappedPort(3306));
  }

  @Override
  public MockMvc mvc() {
    return this.mvc;
  }

  @Test
  public void asACatalogAdminIShouldBeAbleToCreateANewCastMemberWithValidValues() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, castMemberRepository.count());

    final var expectedName = Fixture.name();
    final var expectedType = Fixture.CastMember.type();

    final var actualMemberId = givenACastMember(expectedName, expectedType);

    final var actualMember = castMemberRepository.findById(actualMemberId.getValue()).get();

    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertNotNull(actualMember.getCreatedAt());
    Assertions.assertNotNull(actualMember.getUpdatedAt());
    Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());
  }

  @Test
  public void
      asACatalogAdminIShouldBeAbleToSeeATreatedErrorByCreatingANewCastMemberWithInvalidValues()
          throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, castMemberRepository.count());

    final String expectedName = null;
    final var expectedType = Fixture.CastMember.type();
    final var expectedErrorMessage = "'name' should not be null";

    givenACastMemberResult(expectedName, expectedType)
        .andExpect(status().isUnprocessableEntity())
        .andExpect(header().string("Location", nullValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));
  }

  @Test
  public void asACatalogAdminIShouldBeAbleToNavigateThruAllMembers() throws Exception {
    Assertions.assertTrue(MYSQL_CONTAINER.isRunning());
    Assertions.assertEquals(0, castMemberRepository.count());

    givenACastMember("Vin Diesel", CastMemberType.ACTOR);
    givenACastMember("Quentin Tarantino", CastMemberType.DIRECTOR);
    givenACastMember("Jason Momoa", CastMemberType.ACTOR);

    listCastMembers(0, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Jason Momoa")));

    listCastMembers(1, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(1)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Quentin Tarantino")));

    listCastMembers(2, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(2)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Vin Diesel")));

    listCastMembers(3, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(3)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(0)));
  }
}
