package com.fullcycle.admin.catalogo.domain.castmember;

import com.fullcycle.admin.catalogo.domain.validation.ValidationHandler;
import com.fullcycle.admin.catalogo.domain.validation.Validator;

public class CastMemberValidator extends Validator {

  private final CastMember castMember;

  public CastMemberValidator(final CastMember aMember, final ValidationHandler aHandler) {
    super(aHandler);
    this.castMember = aMember;
  }

  @Override
  public void validate() {}
}
