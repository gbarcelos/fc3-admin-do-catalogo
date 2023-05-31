package com.fullcycle.admin.catalogo.application.castmember.create;

import com.fullcycle.admin.catalogo.application.UseCase;

public abstract sealed class CreateCastMemberUseCase
    extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
    permits DefaultCreateCastMemberUseCase {}
