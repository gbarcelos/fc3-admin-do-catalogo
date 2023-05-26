package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreOutput;
import com.fullcycle.admin.catalogo.application.genre.create.CreateGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.delete.DeleteGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GenreOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.get.GetGenreByIdUseCase;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.GenreListOutput;
import com.fullcycle.admin.catalogo.application.genre.retrieve.list.ListGenreUseCase;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreOutput;
import com.fullcycle.admin.catalogo.application.genre.update.UpdateGenreUseCase;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotFoundException;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.CreateGenreRequest;
import com.fullcycle.admin.catalogo.infrastructure.genre.models.UpdateGenreRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ControllerTest(controllers = GenreAPI.class)
public class GenreAPITest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper mapper;

  @MockBean private CreateGenreUseCase createGenreUseCase;

  @Test
  public void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() throws Exception {
    // given
    final var expectedName = "Ação";
    final var expectedCategories = List.of("123", "456");
    final var expectedIsActive = true;
    final var expectedId = "123";

    final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

    when(createGenreUseCase.execute(any())).thenReturn(CreateGenreOutput.from(expectedId));

    // when
    final var aRequest =
        post("/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(aCommand));

    final var response = this.mvc.perform(aRequest).andDo(print());

    // then
    response
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", "/genres/" + expectedId))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.id", equalTo(expectedId)));

    verify(createGenreUseCase)
        .execute(
            argThat(
                cmd ->
                    Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())));
  }

  @Test
  public void givenAnInvalidName_whenCallsCreateGenre_shouldReturnNotification() throws Exception {
    // given
    final String expectedName = null;
    final var expectedCategories = List.of("123", "456");
    final var expectedIsActive = true;
    final var expectedErrorMessage = "'name' should not be null";

    final var aCommand = new CreateGenreRequest(expectedName, expectedCategories, expectedIsActive);

    when(createGenreUseCase.execute(any()))
        .thenThrow(
            new NotificationException(
                "Error", Notification.create(new Error(expectedErrorMessage))));

    // when
    final var aRequest =
        post("/genres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(this.mapper.writeValueAsString(aCommand));

    final var response = this.mvc.perform(aRequest).andDo(print());

    // then
    response
        .andExpect(status().isUnprocessableEntity())
        .andExpect(header().string("Location", nullValue()))
        .andExpect(header().string("Content-Type", MediaType.APPLICATION_JSON_VALUE))
        .andExpect(jsonPath("$.errors", hasSize(1)))
        .andExpect(jsonPath("$.errors[0].message", equalTo(expectedErrorMessage)));

    verify(createGenreUseCase)
        .execute(
            argThat(
                cmd ->
                    Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedCategories, cmd.categories())
                        && Objects.equals(expectedIsActive, cmd.isActive())));
  }
}
