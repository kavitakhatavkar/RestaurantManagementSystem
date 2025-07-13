package org.example;

import org.example.controllers.OrderController;
import org.example.dtos.PlaceOrderRequestDTO;
import org.example.dtos.PlaceOrderResponseDTO;
import org.example.dtos.ResponseStatus;
import org.example.models.*;
import org.example.repositories.CustomerSessionRepository;
import org.example.repositories.MenuItemRepository;
import org.example.repositories.OrderRepository;
import org.example.repositories.UserRepository;
import org.example.services.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.lang.reflect.Constructor;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestPlaceOrderController {
    private OrderController orderController;
    private OrderRepository orderRepository;
    private UserRepository userRepository;
    private MenuItemRepository menuItemRepository;
    private CustomerSessionRepository customerSessionRepository;
    private OrderService orderService;


    @BeforeEach
    public void setupTest() throws Exception {
        initializeComponents();
    }

    private void initializeComponents() throws Exception {
        initializeRepositories();
        initializeOrderService();
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
        this.menuItemRepository = createInstance(MenuItemRepository.class, repositoryReflections);
        this.userRepository = createInstance(UserRepository.class, repositoryReflections);
        this.customerSessionRepository = createInstance(CustomerSessionRepository.class, repositoryReflections);
        this.orderRepository = createInstance(OrderRepository.class, repositoryReflections);
    }


    private void initializeOrderService() throws Exception {
        Reflections serviceReflections = new Reflections(OrderService.class.getPackageName(), new SubTypesScanner(false));
        this.orderService = createInstanceWithArgs(OrderService.class, serviceReflections, Arrays.asList(this.menuItemRepository, this.userRepository, this.customerSessionRepository, this.orderRepository));
    }

    private void initializeTicketController() {
        this.orderController = new OrderController(this.orderService);
    }


    @Test
    public void testPlaceOrder_Success(){
        User user = setupUser();
        List<MenuItem> menuItems = setupMenuItems();
        PlaceOrderRequestDTO requestDto = new PlaceOrderRequestDTO();
        requestDto.setUserId(user.getId());
        Map<Long, Integer> orderedItems = new HashMap<>();
        orderedItems.put(menuItems.get(0).getId(), 2);
        orderedItems.put(menuItems.get(1).getId(), 1);

        requestDto.setOrderedItems(orderedItems);
        PlaceOrderResponseDTO placeOrderResponseDto = orderController.placeOrder(requestDto);
        assertEquals(placeOrderResponseDto.getResponseStatus(), ResponseStatus.SUCCESS, "Place order should be successful");
        assertNotNull(placeOrderResponseDto.getOrder(), "Order should not be null");
        Order order = placeOrderResponseDto.getOrder();
        assertEquals(2, order.getOrderedItems().size(), "Order should have 2 items");

        order.getOrderedItems().forEach((menuItem, quantity) -> {
            if(menuItem.getId() == menuItems.get(0).getId()){
                assertEquals(2, quantity, "Quantity of Paneer Tikka should be 2");
            } else if(menuItem.getId() == menuItems.get(1).getId()){
                assertEquals(1, quantity, "Quantity of Chicken Tikka should be 1");
            } else {
                fail("Invalid menu item");
            }
        });

        this.customerSessionRepository.findActiveCustomerSessionByUserId(user.getId()).ifPresent(customerSession -> {
            assertEquals(CustomerSessionStatus.ACTIVE, customerSession.getCustomerSessionStatus(), "Customer session status should be active");
        });
    }

    @Test
    public void testPlaceOrder_UserDoesntExists(){
        User user = setupUser();
        List<MenuItem> menuItems = setupMenuItems();
        PlaceOrderRequestDTO requestDto = new PlaceOrderRequestDTO();
        requestDto.setUserId(user.getId() + 1);
        Map<Long, Integer> orderedItems = new HashMap<>();
        orderedItems.put(menuItems.get(0).getId(), 2);
        orderedItems.put(menuItems.get(1).getId(), 1);

        requestDto.setOrderedItems(orderedItems);
        PlaceOrderResponseDTO placeOrderResponseDto = orderController.placeOrder(requestDto);
        assertEquals(placeOrderResponseDto.getResponseStatus(), ResponseStatus.FAILURE, "Place order should be failure");
        assertNull(placeOrderResponseDto.getOrder(), "Order should be null");
    }

    @Test
    public void testPlaceOrder_InvalidMenuItem(){
        User user = setupUser();
        List<MenuItem> menuItems = setupMenuItems();
        PlaceOrderRequestDTO requestDto = new PlaceOrderRequestDTO();
        requestDto.setUserId(user.getId());
        Map<Long, Integer> orderedItems = new HashMap<>();
        orderedItems.put(menuItems.get(0).getId()+100, 2);
        orderedItems.put(menuItems.get(1).getId()+ 100, 1);

        requestDto.setOrderedItems(orderedItems);
        PlaceOrderResponseDTO placeOrderResponseDto = orderController.placeOrder(requestDto);
        assertEquals(placeOrderResponseDto.getResponseStatus(), ResponseStatus.FAILURE, "Place order should be failure");
        assertNull(placeOrderResponseDto.getOrder(), "Order should be null");
    }


    public User setupUser(){
        User user = new User();
        user.setName("Test User");
        user.setPhone("1234567890");
        user.setUserType(UserType.CUSTOMER);
        return userRepository.save(user);
    }

    public List<MenuItem> setupMenuItems(){
        List<MenuItem> menuItems = new ArrayList<>();
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Paneer Tikka");
        menuItem.setPrice(200);
        menuItem.setDietaryRequirement(DietaryRequirement.VEG);
        menuItem.setItemType(ItemType.REGULAR);
        menuItem.setDescription("Paneer Tikka is a vegetarian dish from the Indian subcontinent made from paneer marinated in spices and grilled in a tandoor.");
        menuItems.add(menuItemRepository.add(menuItem));

        menuItem = new MenuItem();
        menuItem.setName("Chicken Tikka");
        menuItem.setPrice(300);
        menuItem.setDietaryRequirement(DietaryRequirement.NON_VEG);
        menuItem.setItemType(ItemType.REGULAR);
        menuItem.setDescription("Chicken tikka is a chicken dish originating in the Indian subcontinent; the dish is popular in India, Bangladesh and Pakistan.");
        menuItems.add(menuItemRepository.add(menuItem));

        menuItem = new MenuItem();
        menuItem.setName("Chicken Tikka Masala");
        menuItem.setPrice(400);
        menuItem.setDietaryRequirement(DietaryRequirement.NON_VEG);
        menuItem.setItemType(ItemType.REGULAR);
        menuItem.setDescription("Chicken tikka masala is a dish consisting of roasted marinated chicken chunks (chicken tikka) in spiced curry sauce.");
        menuItems.add(menuItemRepository.add(menuItem));

        menuItem = new MenuItem();
        menuItem.setName("Paneer Tandoori");
        menuItem.setPrice(250);
        menuItem.setDietaryRequirement(DietaryRequirement.VEG);
        menuItem.setItemType(ItemType.REGULAR);
        menuItem.setDescription("Paneer tikka is an Indian dish made from chunks of paneer marinated in spices and grilled in a tandoor.");
        menuItems.add(menuItemRepository.add(menuItem));

        menuItem = new MenuItem();
        menuItem.setName("Paneer Tikka Masala");
        menuItem.setPrice(350);
        menuItem.setDietaryRequirement(DietaryRequirement.VEG);
        menuItem.setItemType(ItemType.REGULAR);
        menuItem.setDescription("Paneer tikka masala is an Indian dish of marinated paneer cheese served in a spiced gravy.");
        menuItems.add(menuItemRepository.add(menuItem));

        return menuItems;
    }
}
