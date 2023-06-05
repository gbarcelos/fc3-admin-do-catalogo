package com.fullcycle.admin.catalogo.infrastructure.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fullcycle.admin.catalogo.ControllerTest;
import com.fullcycle.admin.catalogo.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import com.fullcycle.admin.catalogo.application.castmember.retrieve.list.ListCastMembersUseCase;
import com.fullcycle.admin.catalogo.application.castmember.update.UpdateCastMemberUseCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@ControllerTest(controllers = CastMemberAPI.class)
public class CastMemberAPITest {

  @Autowired private MockMvc mvc;

  @Autowired private ObjectMapper mapper;

  @MockBean private CreateCastMemberUseCase createCastMemberUseCase;

  @MockBean private DeleteCastMemberUseCase deleteCastMemberUseCase;

  @MockBean private GetCastMemberByIdUseCase getCastMemberByIdUseCase;

  @MockBean private ListCastMembersUseCase listCastMembersUseCase;

  @MockBean private UpdateCastMemberUseCase updateCastMemberUseCase;
}
