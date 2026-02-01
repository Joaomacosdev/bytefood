package br.com.bytefood.menu.service;

import br.com.bytefood.menu.dtos.MenuDTO;
import br.com.bytefood.response.Response;

import java.util.List;

public interface MenuService {

    Response<MenuDTO> createMenu(MenuDTO menuDTO);
    Response<MenuDTO> updateMenu(MenuDTO menuDTO);
    Response<MenuDTO> getMenuById(Long id);
    Response<?> deleteMenu(Long id);
    Response<List<MenuDTO>> getMenus(Long categoryId, String search);
}
