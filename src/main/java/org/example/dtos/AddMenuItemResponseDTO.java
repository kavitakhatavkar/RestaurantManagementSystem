package org.example.dtos;

import org.example.models.MenuItem;

public class AddMenuItemResponseDTO {
    private ResponseStatus status;
    private MenuItem menuItem;

    public ResponseStatus getStatus() {
        return status;
    }

    public void setStatus(ResponseStatus status) {
        this.status = status;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(MenuItem menuItem) {
        this.menuItem = menuItem;
    }
}
