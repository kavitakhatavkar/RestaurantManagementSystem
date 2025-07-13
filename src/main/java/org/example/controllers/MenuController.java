package org.example.controllers;

import org.example.dtos.GetMenuItemsRequestDTO;
import org.example.dtos.GetMenuItemsResponseDTO;
import org.example.dtos.ResponseStatus;
import org.example.models.MenuItem;
import org.example.services.MenuService;

import java.util.List;

public class MenuController {
    private MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }
    public GetMenuItemsResponseDTO getMenuItems(GetMenuItemsRequestDTO requestDto){
        GetMenuItemsResponseDTO responseDTO = new GetMenuItemsResponseDTO();
        try {
            List<MenuItem> menuItems = menuService.getMenuItems(requestDto.getDietaryRequirement());
            responseDTO.setMenuItems(menuItems);
            responseDTO.setResponseStatus(ResponseStatus.SUCCESS);
        } catch (Exception exception) {
            responseDTO.setResponseStatus(ResponseStatus.FAILURE);
        }
        return responseDTO;
    }
}
