package org.example.repositories;

import org.example.models.DietaryRequirement;
import org.example.models.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MenuRepositoryImpl implements MenuRepository {
    private List<MenuItem> menuItems;

    public MenuRepositoryImpl() {
        this.menuItems = new ArrayList<>();
    }

    @Override
    public MenuItem add(MenuItem menuItem) {
        menuItems.add(menuItem);
        return menuItem;
    }

    @Override
    public List<MenuItem> getAll() {
        return menuItems;
    }

    @Override
    public List<MenuItem> getByDietaryRequirement(DietaryRequirement dietaryRequirement) {
        return menuItems.stream().filter(menuItem -> menuItem.getDietaryRequirement().equals(dietaryRequirement)).collect(Collectors.toList());
    }

    @Override
    public MenuItem save(MenuItem menuItem) {
        menuItems.add(menuItem);
        return menuItem;
    }
}
