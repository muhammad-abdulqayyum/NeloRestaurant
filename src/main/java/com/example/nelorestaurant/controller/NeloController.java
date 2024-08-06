package com.example.nelorestaurant.controller;

import com.example.nelorestaurant.model.Diner;
import com.example.nelorestaurant.model.Reservation;
import com.example.nelorestaurant.model.Table;
import com.example.nelorestaurant.request.CreateReservationRequest;
import com.example.nelorestaurant.request.SearchReservationRequest;
import com.example.nelorestaurant.service.NeloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/reservations")
public class NeloController {

    private static final Logger log = LoggerFactory.getLogger(NeloController.class);

    @Autowired
    NeloService neloService;

    @PostMapping("/search")
    public ResponseEntity<List<Table>> findAvailableRestaurantTables(@RequestBody SearchReservationRequest request){

        int groupSize = request.getGroupSize();
        LocalDateTime requestTime = request.getTime();
        List<String> dietaryRestrictions = request.getDietaryRestrictions();

        try{
            // Gets Available Tables by Group Size, RequestTime and Dietary Restrictions
            List<Table> availableTables = neloService.getMatchingTables(groupSize, requestTime, dietaryRestrictions);

            if(availableTables != null && !availableTables.isEmpty()){
                return ResponseEntity.ok(availableTables);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }

    }

    @PostMapping("/createReservation")
    public ResponseEntity<Reservation> createReservation(@RequestBody CreateReservationRequest request){

        // request data for availableTables
        int groupSize = request.getDinerInfo().size();
        LocalDateTime requestTime = request.getReservationTime();
        List<String> dietaryRestrictions = request.getDietaryRestrictions();

        // request data to createReservation
        int tableId = request.getTableId();
        int restaurantId = request.getRestaurantId();
        Set<Diner> guestInfo = request.getDinerInfo();

        //add restrictions for each unique guest
        for(Diner guest : guestInfo){
            dietaryRestrictions.addAll(guest.getDietaryRestrictions());
        }

        try{
            List<Table> availableTables = neloService.getMatchingTables(groupSize, requestTime, dietaryRestrictions);

            if(availableTables != null && !availableTables.isEmpty()){
                //TODO: Now, create Reservation
                // include: tableId, restaurantId, requestTime

                Reservation reservation = neloService.createReservation(groupSize, requestTime, dietaryRestrictions);

                return ResponseEntity.ok(reservation);

            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/deleteReservation/{reservationId}")
    public ResponseEntity<String> deleteReservation(@PathVariable int reservationId){

        try{
            boolean deleteSuccessful = neloService.deleteReservation(reservationId);

            if(deleteSuccessful){
                log.info("Reservation successfully deleted");
                return ResponseEntity.ok("Reservation ID: " + reservationId + " successfully deleted");
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Error while deleting reservation ID: " + reservationId, e);
            return ResponseEntity.badRequest().build();
        }
    }

}
