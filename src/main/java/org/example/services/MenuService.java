package org.example.services;

import org.example.exceptions.UnAuthorizedAccess;
import org.example.exceptions.UserNotFoundException;
import org.example.models.MenuItem;

import java.util.List;

public interface MenuService {
    List<MenuItem> getMenuItems(String itemType);
    MenuItem addMenuItem(long userId, String name, double price, String dietaryRequirement, String itemType, String description) throws UserNotFoundException, UnAuthorizedAccess;
}
