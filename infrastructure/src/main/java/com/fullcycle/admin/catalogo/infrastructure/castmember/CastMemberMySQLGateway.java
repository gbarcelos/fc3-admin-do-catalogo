package com.fullcycle.admin.catalogo.infrastructure.castmember;

import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalogo.domain.pagination.Pagination;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

  private final CastMemberRepository castMemberRepository;

  public CastMemberMySQLGateway(final CastMemberRepository castMemberRepository) {
    this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
  }

  @Override
  public CastMember create(CastMember aCastMember) {
    return save(aCastMember);
  }

  @Override
  public void deleteById(CastMemberID anId) {}

  @Override
  public Optional<CastMember> findById(CastMemberID anId) {
    return Optional.empty();
  }

  @Override
  public CastMember update(CastMember aCastMember) {
    return save(aCastMember);
  }

  @Override
  public Pagination<CastMember> findAll(SearchQuery aQuery) {
    return null;
  }

  private CastMember save(final CastMember aCastMember) {
    return this.castMemberRepository.save(CastMemberJpaEntity.from(aCastMember)).toAggregate();
  }
}
