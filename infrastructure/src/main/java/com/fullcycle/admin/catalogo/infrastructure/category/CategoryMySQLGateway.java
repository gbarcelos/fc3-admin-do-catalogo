package com.fullcycle.admin.catalogo.infrastructure.category;

import static com.fullcycle.admin.catalogo.infrastructure.utils.SpecificationUtils.like;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.category.CategoryID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CategoryMySQLGateway implements CategoryGateway {

  private final CategoryRepository repository;

  public CategoryMySQLGateway(final CategoryRepository repository) {
    this.repository = repository;
  }

  @Override
  public Category create(final Category aCategory) {
    return save(aCategory);
  }

  @Override
  public void deleteById(final CategoryID anId) {
    final String anIdValue = anId.getValue();
    if (repository.existsById(anIdValue)) {
      repository.deleteById(anIdValue);
    }
  }

  @Override
  public Optional<Category> findById(final CategoryID anId) {
    return repository.findById(anId.getValue()).map(CategoryJpaEntity::toAggregate);
  }

  @Override
  public Category update(final Category aCategory) {
    return save(aCategory);
  }

  @Override
  public Pagination<Category> findAll(final SearchQuery aQuery) {

    // Paginação
    final var page =
        PageRequest.of(
            aQuery.page(),
            aQuery.perPage(),
            Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort()));

    // Busca dinamica pelo criterio terms (name ou description)
    final var specifications =
        Optional.ofNullable(aQuery.terms())
            .filter(str -> !str.isBlank())
            .map(this::assembleSpecification)
            .orElse(null);

    final var pageResult = this.repository.findAll(Specification.where(specifications), page);

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(CategoryJpaEntity::toAggregate).toList());
  }

  @Override
  public List<CategoryID> existsByIds(final Iterable<CategoryID> ids) {
    // TODO: implementar quando chegar na camada de infra
    return Collections.emptyList();
  }

  private Category save(final Category aCategory) {
    return repository.save(CategoryJpaEntity.from(aCategory)).toAggregate();
  }

  private Specification<CategoryJpaEntity> assembleSpecification(final String str) {
    final Specification<CategoryJpaEntity> nameLike = like("name", str);
    final Specification<CategoryJpaEntity> descriptionLike = like("description", str);
    return nameLike.or(descriptionLike);
  }
}
