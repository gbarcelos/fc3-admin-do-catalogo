package com.fullcycle.admin.catalogo.application.castmember.retrieve.get;

import com.fullcycle.admin.catalogo.application.UseCase;

public abstract sealed class GetCastMemberByIdUseCase extends UseCase<String, CastMemberOutput>
    permits DefaultGetCastMemberByIdUseCase {}
