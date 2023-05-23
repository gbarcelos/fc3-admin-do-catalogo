package com.fullcycle.admin.catalogo.infrastructure.genre;

import com.fullcycle.admin.catalogo.domain.genre.Genre;
import com.fullcycle.admin.catalogo.domain.genre.GenreGateway;
import com.fullcycle.admin.catalogo.domain.genre.GenreID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.genre.persistence.GenreRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class GenreMySQLGateway implements GenreGateway {

  private final GenreRepository genreRepository;

  public GenreMySQLGateway(final GenreRepository genreRepository) {
    this.genreRepository = Objects.requireNonNull(genreRepository);
  }

  @Override
  public Genre create(final Genre aGenre) {
    return save(aGenre);
  }

  @Override
  public void deleteById(GenreID anId) {
    final var aGenreId = anId.getValue();
    if (this.genreRepository.existsById(aGenreId)) {
      this.genreRepository.deleteById(aGenreId);
    }
  }

  @Override
  public Optional<Genre> findById(final GenreID anId) {
    return this.genreRepository.findById(anId.getValue()).map(GenreJpaEntity::toAggregate);
  }

  @Override
  public Genre update(Genre aGenre) {
    return save(aGenre);
  }

  @Override
  public Pagination<Genre> findAll(SearchQuery aQuery) {
    return null;
  }

  private Genre save(final Genre aGenre) {
    return this.genreRepository.save(GenreJpaEntity.from(aGenre)).toAggregate();
  }
}
