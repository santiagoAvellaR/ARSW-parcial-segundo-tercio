package edu.eci.arsw.myrestaurant.services.DTOs;

import edu.eci.arsw.myrestaurant.model.Order;

import java.util.Map;


public class OrderDTO {
    private int tableNumber;

    private int total;
    private Map<String, Integer> orderInformation;

    public OrderDTO(int tableNumber, int total, Map<String, Integer> orderInformation){
        this.total = total;
        this.tableNumber = tableNumber;
        this.orderInformation = orderInformation;
    }

}
