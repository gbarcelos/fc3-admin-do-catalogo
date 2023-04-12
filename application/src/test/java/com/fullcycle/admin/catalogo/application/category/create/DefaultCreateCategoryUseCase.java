package com.fullcycle.admin.catalogo.application.category.create;

import com.fullcycle.admin.catalogo.domain.category.Category;
import com.fullcycle.admin.catalogo.domain.category.CategoryGateway;
import com.fullcycle.admin.catalogo.domain.validation.handler.ThrowsValidationHandler;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase {

  private final CategoryGateway categoryGateway;

  public DefaultCreateCategoryUseCase(final CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }

  @Override
  public CreateCategoryOutput execute(final CreateCategoryCommand aCommand) {

    final var aCategory =
        Category.newCategory(aCommand.name(), aCommand.description(), aCommand.isActive());
    aCategory.validate(new ThrowsValidationHandler());

    return CreateCategoryOutput.from(this.categoryGateway.create(aCategory));
  }
}
