package com.fullcycle.admin.catalogo.application.castmember.delete;

import com.fullcycle.admin.catalogo.application.UnitUseCase;

public abstract sealed class DeleteCastMemberUseCase extends UnitUseCase<String>
    permits DefaultDeleteCastMemberUseCase {}
