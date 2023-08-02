package com.fullcycle.admin.catalogo.application.castmember.update;

import com.fullcycle.admin.catalogo.application.UseCase;

public abstract sealed class UpdateCastMemberUseCase
    extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
    permits DefaultUpdateCastMemberUseCase {}
