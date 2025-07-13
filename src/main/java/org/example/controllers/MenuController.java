package org.example.controllers;

import org.example.dtos.*;
import org.example.exceptions.UnAuthorizedAccess;
import org.example.exceptions.UserNotFoundException;
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

    public AddMenuItemResponseDTO addMenuItem(AddMenuItemRequestDTO requestDto){
        AddMenuItemResponseDTO responseDto = new AddMenuItemResponseDTO();
        try {
            MenuItem menuItem = menuService.addMenuItem(requestDto.getUserId(), requestDto.getName(), requestDto.getPrice(), requestDto.getDietaryRequirement(), requestDto.getItemType(), requestDto.getDescription());
            responseDto.setMenuItem(menuItem);
            responseDto.setStatus(ResponseStatus.SUCCESS);
        } catch (UserNotFoundException e) {
            responseDto.setStatus(ResponseStatus.FAILURE);
        } catch (UnAuthorizedAccess e) {
            responseDto.setStatus(ResponseStatus.FAILURE);
        } catch (IllegalArgumentException e) {
            responseDto.setStatus(ResponseStatus.FAILURE);
        } catch (Exception e) {
            responseDto.setStatus(ResponseStatus.FAILURE);
        }
        return responseDto;
    }
}
