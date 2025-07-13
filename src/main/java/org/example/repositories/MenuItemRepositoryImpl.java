package org.example.repositories;

import org.example.models.MenuItem;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class MenuItemRepositoryImpl implements MenuItemRepository {
    private final Map<Long, MenuItem> menuItems;
    private static long counter;
    public MenuItemRepositoryImpl() {
        this.menuItems = new HashMap<>();
        counter = 1;
    }
    @Override
    public MenuItem add(MenuItem menuItem) {
        menuItem.setId(counter++);
        menuItems.put(menuItem.getId(), menuItem);
        return menuItem;
    }
    @Override
    public Optional<MenuItem> findById(long id) {
        return Optional.ofNullable(menuItems.get(id));
    }
}
