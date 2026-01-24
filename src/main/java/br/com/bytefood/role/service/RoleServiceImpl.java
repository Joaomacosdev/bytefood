package br.com.bytefood.role.service;

import br.com.bytefood.exception.BadRequestException;
import br.com.bytefood.exception.NotFoundException;
import br.com.bytefood.response.Response;
import br.com.bytefood.role.dtos.RoleDTO;
import br.com.bytefood.role.entity.Role;
import br.com.bytefood.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    @Override
    public Response<RoleDTO> createRole(RoleDTO roleDTO) {
        Role role = modelMapper.map(roleDTO, Role.class);

        Role savedRole = roleRepository.save(role);

        return Response.<RoleDTO>builder()
                .statusCode(HttpStatus.CREATED.value())
                .message("Role created successfully")
                .data(modelMapper.map(savedRole, RoleDTO.class))
                .build();
    }

    @Override
    public Response<RoleDTO> updateRole(RoleDTO roleDTO) {
        Role existingRole = roleRepository.findById(roleDTO.getId())
                .orElseThrow(() -> new NotFoundException("Role not found"));

        if (roleRepository.findByName(roleDTO.getName()).isPresent()){
            throw new BadRequestException("Role with name already exists");
        }

        existingRole.setName(roleDTO.getName());
        Role updateRole = roleRepository.save(existingRole);

        return Response.<RoleDTO>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Role update successfully")
                .data(modelMapper.map(updateRole, RoleDTO.class))
                .build();
    }

    @Override
    public Response<List<RoleDTO>> getAllRole() {

        List<Role> roles = roleRepository.findAll();
        List<RoleDTO> roleDTOS = roles.stream()
                .map(role -> modelMapper.map(role, RoleDTO.class))
                .toList();

        return  Response.<List<RoleDTO>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Roles retrieved successfully")
                .data(roleDTOS)
                .build();
    }

    @Override
    public Response<?> deleteRole(Long id) {
        if (!roleRepository.existsById(id)){
            throw new NotFoundException("Role not found");
        }

        roleRepository.deleteById(id);
        return  Response.builder()
                .statusCode(HttpStatus.NO_CONTENT.value())
                .message("Role delete successfully")
                .build();
    }
}
