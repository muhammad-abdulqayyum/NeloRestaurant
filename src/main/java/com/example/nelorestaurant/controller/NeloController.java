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
            //Todo: need to add logic to ensure tables are open for given time
            //Todo: can do this by getting reservations where time is valid.
            // Then loading all table ids for those reservations in set
            // then, checking if each available table is within set. If it is not, remove from available tables.
            // quick fix for now but room for optimization in future by making more defined sql queries, accross the board.
            // working towards MVP now, minimum viable product. Just Do It

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

        int groupSize = request.getDinerInfo().size();
        int tableId = request.getTableId();
        int restaurantId = request.getRestaurantId();
        LocalDateTime requestTime = request.getReservationTime();

        Set<Diner> guestInfo = request.getDinerInfo();

        List<String> dietaryRestrictions = request.getDietaryRestrictions();

        //add restrictions for each unique guest
        for(Diner guest : guestInfo){
            dietaryRestrictions.addAll(guest.getDietaryRestrictions());
        }

        try{
            List<Table> availableTables = neloService.getMatchingTables(groupSize, requestTime, dietaryRestrictions);

            if(availableTables != null && !availableTables.isEmpty()){
                //TODO: Now, create Reservation

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
