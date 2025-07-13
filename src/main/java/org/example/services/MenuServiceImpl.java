package org.example.services;

import org.example.exceptions.UnAuthorizedAccess;
import org.example.exceptions.UserNotFoundException;
import org.example.models.*;
import org.example.repositories.MenuRepository;
import org.example.repositories.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuServiceImpl implements MenuService {
    private MenuRepository menuRepository;
    private UserRepository userRepository;
    public MenuServiceImpl(MenuRepository menuRepository, UserRepository userRepository) {
        this.menuRepository = menuRepository;
        this.userRepository = userRepository;
    }
    @Override
    public List<MenuItem> getMenuItems(String itemType) {
        List<MenuItem> menuItems = new ArrayList<>();
        if(itemType == null || itemType.isEmpty()) {
            return menuRepository.getAll();
        }

        if (itemType.equals(DietaryRequirement.VEG.name()) ||
                itemType.equals(DietaryRequirement.NON_VEG.name()) ||
                itemType.equals(DietaryRequirement.VEGAN.name())) {
            menuItems = menuRepository.getByDietaryRequirement(DietaryRequirement.valueOf(itemType));
        }
        else {
            throw new RuntimeException("Invalid dietary preference");
        }
        return menuItems;
    }

    @Override
    public MenuItem addMenuItem(long userId, String name, double price, String dietaryRequirement, String itemType, String description) throws UserNotFoundException, UnAuthorizedAccess {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new UserNotFoundException("Invalid userID");
        }
        User user = userOptional.get();
        if (!user.getUserType().equals(UserType.ADMIN)) {
            throw new UnAuthorizedAccess("Non-Admin users cannot add items to menu.");
        }

        DietaryRequirement dietaryReq;
        try {
            dietaryReq = DietaryRequirement.valueOf(dietaryRequirement.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid dietary requirement: " + dietaryRequirement);
        }

        ItemType itemTyp;
        try {
            itemTyp = ItemType.valueOf(itemType.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid item type: " + itemType);
        }

        MenuItem menuItem = new MenuItem();
        menuItem.setName(name);
        menuItem.setPrice(price);
        menuItem.setDietaryRequirement(dietaryReq);
        menuItem.setItemType(itemTyp);
        menuItem.setDescription(description);

        return menuRepository.add(menuItem);
    }
}
