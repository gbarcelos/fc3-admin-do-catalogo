package com.fullcycle.admin.catalogo.application.castmember.retrieve.list;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.domain.Fixture;
import com.fullcycle.admin.catalogo.domain.castmember.CastMember;
import com.fullcycle.admin.catalogo.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalogo.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalogo.infrastructure.castmember.persistence.CastMemberRepository;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

@IntegrationTest
public class ListCastMembersUseCaseIT {

  @Autowired private ListCastMembersUseCase useCase;

  @Autowired private CastMemberRepository castMemberRepository;

  @SpyBean private CastMemberGateway castMemberGateway;

  @Test
  public void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll()
      throws InterruptedException {
    // given
    final var castMemberOne = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

    Thread.sleep(1);
    final var castMemberTwo = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

    final var members = List.of(castMemberOne, castMemberTwo);

    this.castMemberRepository.saveAllAndFlush(
        members.stream().map(CastMemberJpaEntity::from).toList());

    Assertions.assertEquals(2, this.castMemberRepository.count());

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var expectedItems = members.stream().map(CastMemberListOutput::from).toList();

    final var aQuery =
        new SearchQuery(
            expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualOutput = useCase.execute(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertTrue(
        expectedItems.size() == actualOutput.items().size()
            && expectedItems.containsAll(actualOutput.items()));

    verify(castMemberGateway).findAll(any());
  }

  @Test
  public void givenAValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var expectedItems = List.<CastMemberListOutput>of();

    Assertions.assertEquals(0, this.castMemberRepository.count());

    final var aQuery =
        new SearchQuery(
            expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualOutput = useCase.execute(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    verify(castMemberGateway).findAll(any());
  }

  @Test
  public void
      givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var expectedErrorMessage = "Gateway error";

    doThrow(new IllegalStateException(expectedErrorMessage)).when(castMemberGateway).findAll(any());

    final var aQuery =
        new SearchQuery(
            expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualException =
        Assertions.assertThrows(
            IllegalStateException.class,
            () -> {
              useCase.execute(aQuery);
            });

    // then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    verify(castMemberGateway).findAll(any());
  }
}
