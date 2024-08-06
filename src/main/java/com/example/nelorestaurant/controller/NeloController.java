package com.example.nelorestaurant.controller;

import com.example.nelorestaurant.dto.ReservationDTO;
import com.example.nelorestaurant.model.Diner;
import com.example.nelorestaurant.model.Reservation;
import com.example.nelorestaurant.model.Table;
import com.example.nelorestaurant.request.CreateReservationRequest;
import com.example.nelorestaurant.request.SearchReservationRequest;
import com.example.nelorestaurant.service.NeloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

                ReservationDTO reservationDTO = neloService
                        .createReservation(guestInfo, tableId, restaurantId, requestTime);


                if(reservationDTO != null){

                    Reservation reservation  = new Reservation();
                    reservation.setReservationId(reservationDTO.getReservationId());
                    reservation.setReservationTime(reservationDTO.getReservationTime());
                    reservation.setEndTime(reservationDTO.getEndTime());
                    reservation.setDiners(request.getDinerInfo());

                    for(Table table : availableTables){
                        if(table.getTableId() == tableId){
                            reservation.setTable(table);
                            break;
                        }
                    }

                    return ResponseEntity.ok(reservation);
                } else {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }


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
