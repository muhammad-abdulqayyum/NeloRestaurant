package com.example.nelorestaurant.controller;

import com.example.nelorestaurant.model.Reservation;
import com.example.nelorestaurant.model.Restaurant;
import com.example.nelorestaurant.request.ReservationRequest;
import com.example.nelorestaurant.service.NeloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reservations")
public class NeloController {

    private static final Logger log = LoggerFactory.getLogger(NeloController.class);

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

    @DeleteMapping("/deleteReservation/{reservationId}")
    public ResponseEntity<String> deleteReservation(@PathVariable int reservationId){

        try{
            boolean deleteSuccessful = neloService.deleteReservation(reservationId);

            if(deleteSuccessful){
                log.info("Reservation successfully deleted");
                return ResponseEntity.ok("Reservation ID: " + reservationId + " sucessfully deleted");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error while deleting reservation ID: " + reservationId, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
