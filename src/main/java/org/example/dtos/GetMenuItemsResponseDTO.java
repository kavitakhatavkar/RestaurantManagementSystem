package org.example.dtos;

import org.example.models.MenuItem;

import java.util.List;

public class GetMenuItemsResponseDTO {
    private List<MenuItem> menuItems;
    private ResponseStatus responseStatus;

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public ResponseStatus getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(ResponseStatus responseStatus) {
        this.responseStatus = responseStatus;
    }
}
