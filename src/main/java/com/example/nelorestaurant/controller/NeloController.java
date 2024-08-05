package com.example.nelorestaurant.controller;

import com.example.nelorestaurant.model.Restaurant;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reservations")
public class NeloController {

    @GetMapping("/findTables")
    public List<Restaurant> findAvailableRestaurants(){

        return null;
    }

    ResponseBody createReservation(){

        return null;
    }

    ResponseBody deleteReservation(){

        return null;
    }

}
