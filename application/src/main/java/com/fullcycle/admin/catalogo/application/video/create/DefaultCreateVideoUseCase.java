package com.fullcycle.admin.catalogo.application.video.create;

import com.fullcycle.admin.catalogo.domain.Identifier;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.exceptions.NotificationException;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.validation.Error;
import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.handler.Notification;
import com.fullcycle.admin.catalogo.domain.video.Rating;
import com.fullcycle.admin.catalogo.domain.video.Video;
import com.fullcycle.admin.catalogo.domain.video.VideoGateway;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DefaultCreateVideoUseCase extends CreateVideoUseCase {

  private final CategoryGateway categoryGateway;
  private final CastMemberGateway castMemberGateway;
  private final GenreGateway genreGateway;
  private final VideoGateway videoGateway;

  public DefaultCreateVideoUseCase(
      final CategoryGateway categoryGateway,
      final CastMemberGateway castMemberGateway,
      final GenreGateway genreGateway,
      final VideoGateway videoGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
    this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    this.genreGateway = Objects.requireNonNull(genreGateway);
    this.videoGateway = Objects.requireNonNull(videoGateway);
  }

  @Override
  public CreateVideoOutput execute(CreateVideoCommand aCommand) {

    final var aRating = Rating.of(aCommand.rating()).orElse(null);
    final var aLaunchYear = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
    final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
    final var genres = toIdentifier(aCommand.genres(), GenreID::from);
    final var members = toIdentifier(aCommand.members(), CastMemberID::from);

    final var notification = Notification.create();
    notification.append(validateCategories(categories));
    notification.append(validateGenres(genres));
    notification.append(validateMembers(members));

    final var aVideo =
        Video.newVideo(
            aCommand.title(),
            aCommand.description(),
            aLaunchYear,
            aCommand.duration(),
            aCommand.opened(),
            aCommand.published(),
            aRating,
            categories,
            genres,
            members);

    aVideo.validate(notification);

    if (notification.hasError()) {
      throw new NotificationException("Could not create Aggregate Video", notification);
    }

    return CreateVideoOutput.from(create(aCommand, aVideo));
  }

  private Video create(final CreateVideoCommand aCommand, final Video aVideo) {
    return this.videoGateway.create(aVideo);
  }

  private ValidationHandler validateCategories(final Set<CategoryID> ids) {
    return validateAggregate("categories", ids, categoryGateway::existsByIds);
  }

  private ValidationHandler validateGenres(final Set<GenreID> ids) {
    return validateAggregate("genres", ids, genreGateway::existsByIds);
  }

  private ValidationHandler validateMembers(final Set<CastMemberID> ids) {
    return validateAggregate("cast members", ids, castMemberGateway::existsByIds);
  }

  private <T extends Identifier> ValidationHandler validateAggregate(
      final String aggregate, final Set<T> ids, final Function<Iterable<T>, List<T>> existsByIds) {
    final var notification = Notification.create();
    if (ids == null || ids.isEmpty()) {
      return notification;
    }

    final var retrievedIds = existsByIds.apply(ids);

    if (ids.size() != retrievedIds.size()) {
      final var missingIds = new ArrayList<>(ids);
      missingIds.removeAll(retrievedIds);

      final var missingIdsMessage =
          missingIds.stream().map(Identifier::getValue).collect(Collectors.joining(", "));

      notification.append(
          new Error("Some %s could not be found: %s".formatted(aggregate, missingIdsMessage)));
    }

    return notification;
  }

  private <T> Set<T> toIdentifier(final Set<String> ids, final Function<String, T> mapper) {
    return ids.stream().map(mapper).collect(Collectors.toSet());
  }
}
