package edu.eci.arsw.myrestaurant.services;


import edu.eci.arsw.myrestaurant.model.Order;
import edu.eci.arsw.myrestaurant.model.RestaurantProduct;
import edu.eci.arsw.myrestaurant.beans.BillCalculator;
import edu.eci.arsw.myrestaurant.model.ProductType;
import edu.eci.arsw.myrestaurant.services.DTOs.OrderDTO;
import jdk.internal.org.objectweb.asm.tree.TryCatchBlockNode;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RestaurantOrderServicesStub implements RestaurantOrderServices {

    private static final Map<String, RestaurantProduct> productsMap;

    private static final Map<Integer, Order> tableOrders;
    private BillCalculator calc = null;

    public RestaurantOrderServicesStub(BillCalculator calc) {
        this.calc = calc;
    }

    public void setBillCalculator(BillCalculator calc) {
        this.calc = calc;
    }

    static {
        productsMap = new ConcurrentHashMap<>();
        tableOrders = new ConcurrentHashMap<>();
        productsMap.put("PIZZA", new RestaurantProduct("PIZZA", 10000, ProductType.DISH));
        productsMap.put("HOTDOG", new RestaurantProduct("HOTDOG", 3000, ProductType.DISH));
        productsMap.put("COKE", new RestaurantProduct("COKE", 1300, ProductType.DRINK));
        productsMap.put("HAMBURGER", new RestaurantProduct("HAMBURGER", 12300, ProductType.DISH));
        productsMap.put("BEER", new RestaurantProduct("BEER", 2500, ProductType.DRINK));

        Order o = new Order(1);
        o.addDish("PIZZA", 3);
        o.addDish("HOTDOG", 1);
        o.addDish("COKE", 4);

        tableOrders.put(1, o);

        Order o2 = new Order(3);
        o2.addDish("HAMBURGER", 2);
        o2.addDish("COKE", 2);

        tableOrders.put(3, o2);
    }

    @Override
    public List<OrderDTO> getAllOrders() throws OrderServicesException {
        List<OrderDTO> orderDTOS = new ArrayList<>();
            System.out.println("buscando " + Arrays.toString(tableOrders.keySet().toArray(new Integer[0])));
            for (Integer orderNumber : tableOrders.keySet().toArray(new Integer[0])) {
                System.out.println(orderNumber);
                Order order = tableOrders.get(orderNumber);
                System.out.println(order.toString());
                orderDTOS.add(new OrderDTO(order.getTableNumber(), calculateTableBill(orderNumber), order.getOrderAmountsMap()));
            }
            return orderDTOS;
    }

    @Override
    public Order getTableOrder(int tableNumber) {
        if (!tableOrders.containsKey(tableNumber)) {
            return null;
        } else {
            return tableOrders.get(tableNumber);
        }
    }

    @Override
    public Set<String> getAvailableProductNames() {
        return productsMap.keySet();
    }

    @Override
    public RestaurantProduct getProductByName(String product) throws OrderServicesException {
        if (!productsMap.containsKey(product)) {
            System.out.println("producto no existente");
            throw new OrderServicesException("Producto no existente:" + product);
        } else {
            return productsMap.get(product);
        }
    }

    @Override
    public Set<Integer> getTablesWithOrders() {
        return tableOrders.keySet();
    }

    @Override
    public void addNewOrderToTable(Order o) throws OrderServicesException {
        if (tableOrders.containsKey(o.getTableNumber())) {
            throw new OrderServicesException("La mesa tiene una orden abierta. Debe "
                    + "cerrarse la cuenta antes de crear una nueva.:" + o.getTableNumber());
        } else {
            tableOrders.put(o.getTableNumber(), o);
        }

    }

    @Override
    public void releaseTable(int tableNumber) throws OrderServicesException {
        if (!tableOrders.containsKey(tableNumber)) {
            throw new OrderServicesException("Mesa inexistente o ya liberada:" + tableNumber);
        } else {
            tableOrders.remove(tableNumber);
        }

    }


    @Override
    public int calculateTableBill(int tableNumber) throws OrderServicesException {
        if (!tableOrders.containsKey(tableNumber)) {
            throw new OrderServicesException("Mesa inexistente o ya liberada:" + tableNumber);
        } else {
            return calc.calculateBill(tableOrders.get(tableNumber), productsMap);
        }
    }
    



}
