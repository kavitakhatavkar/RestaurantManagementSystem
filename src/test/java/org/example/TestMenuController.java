package org.example;

import org.example.controllers.MenuController;
import org.example.dtos.*;
import org.example.models.*;
import org.example.repositories.MenuRepository;
import org.example.repositories.UserRepository;
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

import static org.junit.jupiter.api.Assertions.*;

public class TestMenuController {
    private MenuRepository menuRepository;
    private UserRepository userRepository;

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
        Reflections repositoryReflections = new Reflections(UserRepository.class.getPackageName(), new SubTypesScanner(false));
        this.menuRepository = createInstance(MenuRepository.class, repositoryReflections);
        this.userRepository = createInstance(UserRepository.class, repositoryReflections);
    }

    private void initializeMenuService() throws Exception {
        Reflections serviceReflections = new Reflections(MenuService.class.getPackageName(), new SubTypesScanner(false));
        this.menuService = createInstanceWithArgs(MenuService.class, serviceReflections, Arrays.asList(this.menuRepository, this.userRepository));
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

    @Test
    public void testAddMenuItem_Success() throws Exception {
        User adminUser = new User();
        adminUser.setUserType(UserType.ADMIN);
        adminUser.setName("admin");
        adminUser.setPassword("admin");
        adminUser.setPhone("1234567890");
        adminUser = userRepository.save(adminUser);

        AddMenuItemRequestDTO requestDto = new AddMenuItemRequestDTO();
        requestDto.setUserId(adminUser.getId());
        requestDto.setName("Paneer Tikka");
        requestDto.setPrice(200);
        requestDto.setDietaryRequirement("VEG");
        requestDto.setItemType("DAILY_SPECIAL");
        requestDto.setDescription("Paneer Tikka is a vegetarian dish from the Indian subcontinent made from paneer marinated in spices and grilled in a tandoor.");
        AddMenuItemResponseDTO addMenuItemResponseDto = menuController.addMenuItem(requestDto);
        assertEquals(addMenuItemResponseDto.getStatus(), ResponseStatus.SUCCESS, "AddMenuItemResponseDto status should be SUCCESS");
        assertNotNull(addMenuItemResponseDto.getMenuItem(), "AddMenuItemResponseDto menuItem should not be null");
    }

    @Test
    public void testAddMenuItem_AddedByCustomer() throws Exception {
        User customerUser = new User();
        customerUser.setUserType(UserType.CUSTOMER);
        customerUser.setName("admin");
        customerUser.setPassword("admin");
        customerUser.setPhone("1234567890");
        customerUser = userRepository.save(customerUser);

        AddMenuItemRequestDTO requestDto = new AddMenuItemRequestDTO();
        requestDto.setUserId(customerUser.getId());
        requestDto.setName("Paneer Tikka");
        requestDto.setPrice(200);
        requestDto.setDietaryRequirement("VEG");
        requestDto.setItemType("DAILY_SPECIAL");
        requestDto.setDescription("Paneer Tikka is a vegetarian dish from the Indian subcontinent made from paneer marinated in spices and grilled in a tandoor.");
        AddMenuItemResponseDTO addMenuItemResponseDto = menuController.addMenuItem(requestDto);
        assertEquals(addMenuItemResponseDto.getStatus(), ResponseStatus.FAILURE, "AddMenuItemResponseDto status should be Failure as user is not an admin");
        assertNull(addMenuItemResponseDto.getMenuItem(), "AddMenuItemResponseDto menuItem should be null");
    }

    @Test
    public void testAddMenuItem_AddedByNonExistingUser() throws Exception {
        User adminUser = new User();
        adminUser.setUserType(UserType.ADMIN);
        adminUser.setName("admin");
        adminUser.setPassword("admin");
        adminUser.setPhone("1234567890");
        adminUser = userRepository.save(adminUser);

        AddMenuItemRequestDTO requestDto = new AddMenuItemRequestDTO();
        requestDto.setUserId(adminUser.getId() + 1);
        requestDto.setName("Paneer Tikka");
        requestDto.setPrice(200);
        requestDto.setDietaryRequirement("VEG");
        requestDto.setItemType("DAILY_SPECIAL");
        requestDto.setDescription("Paneer Tikka is a vegetarian dish from the Indian subcontinent made from paneer marinated in spices and grilled in a tandoor.");
        AddMenuItemResponseDTO addMenuItemResponseDto = menuController.addMenuItem(requestDto);
        assertEquals(addMenuItemResponseDto.getStatus(), ResponseStatus.FAILURE, "AddMenuItemResponseDto status should be Failure as user does not exist");
        assertNull(addMenuItemResponseDto.getMenuItem(), "AddMenuItemResponseDto menuItem should be null");
    }
}
