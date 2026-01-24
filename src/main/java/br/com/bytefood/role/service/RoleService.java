package br.com.bytefood.role.service;

import br.com.bytefood.response.Response;
import br.com.bytefood.role.dtos.RoleDTO;

import java.util.List;

public interface RoleService {

    Response<RoleDTO> createRole(RoleDTO roleDTO);
    Response<RoleDTO> updateRole(RoleDTO roleDTO);
    Response<List<RoleDTO>> getAllRole();
    Response<?> deleteRole(Long idTO);


}
