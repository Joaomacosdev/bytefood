package br.com.bytefood.role.controller;

import br.com.bytefood.response.Response;
import br.com.bytefood.role.dtos.RoleDTO;
import br.com.bytefood.role.service.RoleServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
//@PreAuthorize("hasAuthority('ADMIN')")
public class RoleController {

    private final RoleServiceImpl roleService;

    @PostMapping
    public ResponseEntity<Response<RoleDTO>> createRole(@RequestBody @Valid RoleDTO roleDTO){
        return ResponseEntity.ok(roleService.createRole(roleDTO));
    }

    @PutMapping
    public ResponseEntity<Response<RoleDTO>> updateRole(@RequestBody @Valid RoleDTO roleDTO){
        return ResponseEntity.ok(roleService.updateRole(roleDTO));
    }

    @GetMapping
    public ResponseEntity<Response<List<RoleDTO>>> getAllRole(){
        return ResponseEntity.ok(roleService.getAllRole());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRole(@PathVariable Long id){
        return ResponseEntity.ok(roleService.deleteRole(id));
    }


}
