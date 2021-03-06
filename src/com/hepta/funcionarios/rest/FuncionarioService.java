package com.hepta.funcionarios.rest;

import com.hepta.funcionarios.entity.Funcionario;
import com.hepta.funcionarios.persistence.FuncionarioDAO;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

@Path("/funcionarios")
public class FuncionarioService {

  @Context
  private HttpServletRequest request;

  @Context
  private HttpServletResponse response;

  private FuncionarioDAO dao;

  public FuncionarioService() {
    dao = new FuncionarioDAO();
  }

  protected void setRequest(HttpServletRequest request) {
    this.request = request;
  }

  /**
   * Adiciona novo Funcionario
   *
   * @param Funcionario: Novo Funcionario
   * @return response 200 (OK) - Conseguiu adicionar
   */
  @Path("/")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @POST
  public Response FuncionarioCreate(@Valid Funcionario funcionario) {
    try {
      dao.save(funcionario);
      return Response.status(Status.OK).build();
    } catch (Exception e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao inserir funcionário").build();
    }
  }

  /**
   * Lista todos os Funcionarios
   *
   * @return response 200 (OK) - Conseguiu listar
   */
  @Path("/")
  @Produces(MediaType.APPLICATION_JSON)
  @GET
  public Response FuncionarioRead() {
    List<Funcionario> Funcionarios = new ArrayList<>();
    try {
      Funcionarios = dao.getAll();
    } catch (Exception e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao buscar Funcionarios").build();
    }

    GenericEntity<List<Funcionario>> entity = new GenericEntity<List<Funcionario>>(Funcionarios) {};
    return Response.status(Status.OK).entity(entity).build();
  }

  /**
   * Atualiza um Funcionario
   *
   * @param id:          id do Funcionario
   * @param funcionario: Funcionario atualizado
   * @return response 200 (OK) - Conseguiu atualizar
   */
  @Path("/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  @PUT	
  public Response FuncionarioUpdate(@Valid  @PathParam("id") Integer id, Funcionario funcionario) {
    try {
      Funcionario funcAtualizar = dao.find(id);
      if (funcAtualizar == null) 
		  return Response.status(Status.NOT_FOUND).entity("Não achou funcionário").build(); 
	  else {
        funcAtualizar = dao.update(new Funcionario(
												  funcAtualizar.getId(),
												  funcionario.getNome(),
												  funcionario.getSetor(),
												  funcionario.getSalario(),
												  funcionario.getEmail(),
												  funcionario.getIdade()
            )
          );

        return Response.status(Status.OK).entity(funcAtualizar).build();
      }
    } catch (Exception e) {
      return Response
        .status(Status.INTERNAL_SERVER_ERROR)
        .entity("Erro ao atualizar funcionário")
        .build();
    }
  }

  /**
   * Remove um Funcionario
   *
   * @param id: id do Funcionario
   * @return response 200 (OK) - Conseguiu remover
   */
  @Path("/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  @DELETE
  public Response FuncionarioDelete(@PathParam("id") Integer id) {
    try {
      dao.delete(id);
      return Response.status(Status.OK).build();
    } catch (Exception e) {
      return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Erro ao deletar funcionário").build();
    }
  }
}
