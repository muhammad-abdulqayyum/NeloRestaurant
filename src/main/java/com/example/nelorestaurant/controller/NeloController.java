package com.example.nelorestaurant.controller;

import com.example.nelorestaurant.model.Reservation;
import com.example.nelorestaurant.model.Restaurant;
import com.example.nelorestaurant.request.ReservationRequest;
import com.example.nelorestaurant.service.NeloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reservations")
public class NeloController {

    @Autowired
    NeloService neloService;

    @PostMapping("/search")
    public ResponseEntity<List<Restaurant>> findAvailableRestaurantTables(@RequestBody ReservationRequest request){


        return ResponseEntity.ok(null);
    }

    @PostMapping("/createReservation")
    public ResponseEntity<Reservation> createReservation(){

        return ResponseEntity.ok(null);
    }

    @PostMapping("/deleteReservation")
    public ResponseEntity<String> deleteReservation(){

        return ResponseEntity.ok(null);
    }

}
