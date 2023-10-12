package com.fullcycle.admin.catalogo.domain.category;

import static com.fullcycle.admin.catalogo.domain.utils.IdUtils.uuid;

import com.fullcycle.admin.catalogo.domain.Identifier;
import java.util.Objects;

public class CategoryID extends Identifier {
  private final String value;

  private CategoryID(final String value) {
    this.value = Objects.requireNonNull(value, "'value' should not be null");
  }

  public static CategoryID unique() {
    return CategoryID.from(uuid());
  }

  public static CategoryID from(final String anId) {
    return new CategoryID(anId);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    final CategoryID that = (CategoryID) o;
    return getValue().equals(that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getValue());
  }

  @Override
  public String getValue() {
    return value;
  }
}
