package org.example;

import org.example.controllers.MenuController;
import org.example.dtos.GetMenuItemsRequestDTO;
import org.example.dtos.GetMenuItemsResponseDTO;
import org.example.dtos.ResponseStatus;
import org.example.models.DietaryRequirement;
import org.example.models.ItemType;
import org.example.models.MenuItem;
import org.example.repositories.MenuRepository;
import org.example.services.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestMenuController {
    private MenuRepository menuRepository;

    private MenuService menuService;

    private MenuController menuController;

    @BeforeEach
    public void setupTest() throws Exception {
        initializeComponents();
    }

    private void initializeComponents() throws Exception {
        initializeRepositories();
        initializeMenuService();
        initializeTicketController();
    }

    private <T> T createInstance(Class<T> interfaceClass, Reflections reflections) throws Exception {
        Set<Class<? extends T>> implementations = reflections.getSubTypesOf(interfaceClass);
        if (implementations.isEmpty()) {
            throw new Exception("No implementation for " + interfaceClass.getSimpleName() + " found");
        }

        Class<? extends T> implementationClass = implementations.iterator().next();
        Constructor<? extends T> constructor = implementationClass.getDeclaredConstructor();
        constructor.setAccessible(true);
        return constructor.newInstance();
    }

    private <T> T createInstanceWithArgs(Class<T> interfaceClass, Reflections reflections, List<Object> dependencies) throws Exception {
        Set<Class<? extends T>> implementations = reflections.getSubTypesOf(interfaceClass);
        if (implementations.isEmpty()) {
            throw new Exception("No implementation for " + interfaceClass.getSimpleName() + " found");
        }
        Class<? extends T> implementationClass = implementations.iterator().next();
        Constructor<?>[] constructors = implementationClass.getConstructors();
        Constructor<?> constructor = Arrays.stream(constructors)
                .filter(constructor1 -> constructor1.getParameterCount() == dependencies.size())
                .findFirst().orElseThrow(() -> new Exception("No constructor with " + dependencies.size() + " arguments found"));
        constructor.setAccessible(true);
        Object[] args = new Object[constructor.getParameterCount()];
        for (int i = 0; i < constructor.getParameterCount(); i++) {
            for (Object dependency : dependencies) {
                if (constructor.getParameterTypes()[i].isInstance(dependency)) {
                    args[i] = dependency;
                    break;
                }
            }
        }
        return (T) constructor.newInstance(args);
    }

    private void initializeRepositories() throws Exception {
        Reflections repositoryReflections = new Reflections(MenuRepository.class.getPackageName(), new SubTypesScanner(false));
        this.menuRepository = createInstance(MenuRepository.class, repositoryReflections);
    }


    private void initializeMenuService() throws Exception {
        Reflections serviceReflections = new Reflections(MenuService.class.getPackageName(), new SubTypesScanner(false));
        this.menuService = createInstanceWithArgs(MenuService.class, serviceReflections, Collections.singletonList(this.menuRepository));
    }

    private void initializeTicketController() {
        this.menuController = new MenuController(this.menuService);
    }


    @Test
    public void testGetMenuItems_WithoutAnyFilter(){
        addFewMenuItems();
        GetMenuItemsRequestDTO requestDto = new GetMenuItemsRequestDTO();
        requestDto.setDietaryRequirement(null);
        GetMenuItemsResponseDTO getMenuItemsResponseDto = menuController.getMenuItems(requestDto);
        assertEquals(getMenuItemsResponseDto.getResponseStatus(), ResponseStatus.SUCCESS, "GetMenuItemsResponseDto status should be SUCCESS");
        assertEquals(getMenuItemsResponseDto.getMenuItems().size(), 6, "GetMenuItemsResponseDto menuItems size should be 6");
    }

    @Test
    public void testGetMenuItems_WithVegFilter(){
        addFewMenuItems();
        GetMenuItemsRequestDTO requestDto = new GetMenuItemsRequestDTO();
        requestDto.setDietaryRequirement("VEG");
        GetMenuItemsResponseDTO getMenuItemsResponseDto = menuController.getMenuItems(requestDto);
        assertEquals(getMenuItemsResponseDto.getResponseStatus(), ResponseStatus.SUCCESS, "GetMenuItemsResponseDto status should be SUCCESS");
        assertEquals(getMenuItemsResponseDto.getMenuItems().size(), 2, "GetMenuItemsResponseDto menuItems size should be 2");
    }

    @Test
    public void testGetMenuItems_WithVeganFilter(){
        addFewMenuItems();
        GetMenuItemsRequestDTO requestDto = new GetMenuItemsRequestDTO();
        requestDto.setDietaryRequirement("VEGAN");
        GetMenuItemsResponseDTO getMenuItemsResponseDto = menuController.getMenuItems(requestDto);
        assertEquals(getMenuItemsResponseDto.getResponseStatus(), ResponseStatus.SUCCESS, "GetMenuItemsResponseDto status should be SUCCESS");
        assertEquals(getMenuItemsResponseDto.getMenuItems().size(), 2, "GetMenuItemsResponseDto menuItems size should be 2");
    }

    @Test
    public void testGetMenuItems_WithNonVegFilter(){
        addFewMenuItems();
        GetMenuItemsRequestDTO requestDto = new GetMenuItemsRequestDTO();
        requestDto.setDietaryRequirement("NON_VEG");
        GetMenuItemsResponseDTO getMenuItemsResponseDto = menuController.getMenuItems(requestDto);
        assertEquals(getMenuItemsResponseDto.getResponseStatus(), ResponseStatus.SUCCESS, "GetMenuItemsResponseDto status should be SUCCESS");
        assertEquals(getMenuItemsResponseDto.getMenuItems().size(), 2, "GetMenuItemsResponseDto menuItems size should be 2");
    }

    private void addFewMenuItems() {

        MenuItem menuItem = new MenuItem();
        menuItem.setName("Paneer Tikka");
        menuItem.setPrice(200);
        menuItem.setDietaryRequirement(DietaryRequirement.VEG);
        menuItem.setItemType(ItemType.DAILY_SPECIAL);
        menuItem.setDescription("Paneer Tikka is a vegetarian dish from the Indian subcontinent made from paneer marinated in spices and grilled in a tandoor.");
        menuRepository.save(menuItem);

        menuItem = new MenuItem();
        menuItem.setName("Chicken Tikka");
        menuItem.setPrice(300);
        menuItem.setDietaryRequirement(DietaryRequirement.NON_VEG);
        menuItem.setItemType(ItemType.DAILY_SPECIAL);
        menuItem.setDescription("Chicken Tikka is a chicken dish originating in the Indian subcontinent; the dish is popular in India, Bangladesh and Pakistan.");
        menuRepository.save(menuItem);

        menuItem = new MenuItem();
        menuItem.setName("Chicken Biryani");
        menuItem.setPrice(300);
        menuItem.setDietaryRequirement(DietaryRequirement.NON_VEG);
        menuItem.setItemType(ItemType.REGULAR);
        menuItem.setDescription("Biryani, also known as biriyani, biriani, birani or briyani, is a mixed rice dish originating among the Muslims of the Indian subcontinent.");
        menuRepository.save(menuItem);


        menuItem = new MenuItem();
        menuItem.setName("Veg Biryani");
        menuItem.setPrice(300);
        menuItem.setDietaryRequirement(DietaryRequirement.VEG);
        menuItem.setItemType(ItemType.REGULAR);
        menuItem.setDescription("Biryani, also known as biriyani, biriani, birani or briyani, is a mixed rice dish originating among the Muslims of the Indian subcontinent.");
        menuItem = menuRepository.save(menuItem);

        menuItem = new MenuItem();
        menuItem.setName("Vegan Tikka Masala");
        menuItem.setPrice(200);
        menuItem.setDietaryRequirement(DietaryRequirement.VEGAN);
        menuItem.setItemType(ItemType.REGULAR);
        menuItem.setDescription("Vegan Tikka Masala is a vegan dish of roasted vegetables in a creamy sauce.");
        menuRepository.save(menuItem);

        menuItem = new MenuItem();
        menuItem.setName("Vegan Biryani");
        menuItem.setPrice(300);
        menuItem.setDietaryRequirement(DietaryRequirement.VEGAN);
        menuItem.setItemType(ItemType.REGULAR);
        menuItem.setDescription("Vegan Biryani is a vegan dish of roasted vegetables in a creamy sauce.");
        menuRepository.save(menuItem);
    }
}
