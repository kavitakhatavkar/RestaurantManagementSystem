package org.example.services;

import org.example.models.MenuItem;

import java.util.List;

public interface MenuService {
    List<MenuItem> getMenuItems(String itemType);
}
