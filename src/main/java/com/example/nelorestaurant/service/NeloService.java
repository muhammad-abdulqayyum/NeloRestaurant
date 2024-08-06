package com.example.nelorestaurant.service;

import com.example.nelorestaurant.dao.NeloRepository;
import com.example.nelorestaurant.dto.ReservationDTO;
import com.example.nelorestaurant.model.Diner;
import com.example.nelorestaurant.model.Reservation;
import com.example.nelorestaurant.model.Restaurant;
import com.example.nelorestaurant.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class NeloService {

    @Autowired
    NeloRepository repository;

    /**
     * Returns List of All Available Tables, based on available group size, dietaryRestrictions and reservationTime
     *
     * @param groupSize
     * @param reservationRequestTime
     * @param dietaryRestrictions
     * @return
     */
    public List<Table> getMatchingTables(int groupSize,
                                         LocalDateTime reservationRequestTime,
                                         List<String> dietaryRestrictions){


        List<Table> openTables = new ArrayList<>();

        if(dietaryRestrictions == null || dietaryRestrictions.isEmpty()){
            // find open tables
            openTables = getOpenTables(groupSize, reservationRequestTime);

        } else {

            //Find Matching Restaurants
            List<Restaurant> allowedRestaurants = getRestaurantsByDietaryRestrictions(dietaryRestrictions);
            Set<Integer> validRestaurantIds = new HashSet<>();

            for (Restaurant restaurant : allowedRestaurants) {
                validRestaurantIds.add(restaurant.getRestaurantId());
            }

            //Find Open Tables - retrive by getting tables
            openTables = getOpenTables(groupSize, reservationRequestTime);

            //Find tables in restaurant that matches dietary restrictions
            List<Table> matchingTables = new ArrayList<>();

            for (Table table : openTables) {
                int restaurantId = table.getRestaurant().getRestaurantId();
                if (validRestaurantIds.contains(restaurantId)) {
                    matchingTables.add(table);
                }
            }
            openTables = matchingTables;
        }
        return openTables;
    }

    /**
     *
     * Method gets all Tables that have capacity for groupSize and is available for specific request time
     *
     * @param groupSize
     * @param time
     * @return
     */
    public List<Table> getOpenTables(int groupSize, LocalDateTime time) {

        List<Table> tablesWithCapacity = repository.getTablesByCapacityFromDB(groupSize, time);
        List<Table> openTables = new ArrayList<>();

        List<Restaurant> allRestaurants = getAllRestaurants();
        HashMap<Integer, Restaurant> restaurantMap = new HashMap<>();

        //Create RestaurantMap / Dictionary
        for (Restaurant restaurant: allRestaurants){
            int restaurantId = restaurant.getRestaurantId();
            restaurantMap.put(restaurantId, restaurant);
        }

        //Fill in Restaurant Data
        for(Table table : tablesWithCapacity){

            int restaurantId = table.getRestaurant().getRestaurantId();
            Restaurant restaurant = restaurantMap.get(restaurantId);
            table.setRestaurant(restaurant);
        }


        LocalDateTime startTime = time;
        LocalDateTime endTime = time.plusHours(2);

        List<Reservation> reservations = repository.getAllReservationsForTime(startTime,endTime);
        Set<Integer> unavailableTableIds = new HashSet<>();

        for(Reservation reservation : reservations){
            int bookedTableId = reservation.getTable().getTableId();
            unavailableTableIds.add(bookedTableId);
        }

        for(Table table : tablesWithCapacity){
            if(!unavailableTableIds.contains(table.getTableId())){
                openTables.add(table);
            }
        }

        return openTables;
    }

    public List<Restaurant> getAllRestaurants() {
        return repository.getAllRestaurants();
    }

    public List<Restaurant> getRestaurantsByDietaryRestrictions(List<String> dietaryRestrictions) {

        List<String> dietaryRestrictionsList = new ArrayList<>(dietaryRestrictions);
        dietaryRestrictionsList.replaceAll(String::toUpperCase);
        Set<String> dietaryRestrictionsSet = new HashSet<>(dietaryRestrictionsList);

        return getRestaurantsByDietaryRestrictions(dietaryRestrictionsSet);
    }

    public List<Restaurant> getRestaurantsByDietaryRestrictions(Set<String> dietaryRestrictions){

        List<Restaurant> restaurants = repository.getAllRestaurants();

        List<Restaurant> matchingRestaurants = new ArrayList<>();

        for (Restaurant restaurant : restaurants) {
            Set<String> endorsements = restaurant.getEndorsements();
            if (endorsements.containsAll(dietaryRestrictions)) {
                matchingRestaurants.add(restaurant);
            }
        }

        return matchingRestaurants;
    }

    public boolean deleteReservation(int reservationId){
        return repository.deleteReservation(reservationId);
    }


    public ReservationDTO createReservation(Set<Diner> guestInfo, int tableId, int restaurantId, LocalDateTime reservationRequestTime) throws SQLException {

        LocalDateTime reservationTime = reservationRequestTime;
        LocalDateTime endTime = reservationRequestTime.plusHours(2);

        boolean isAvailable = repository.isReservationTimeValid(tableId, reservationTime, endTime);

        Set<Integer> dinerIds = new HashSet<>();

        for (Diner diner : guestInfo) {
            dinerIds.add(diner.getDinerId());
        }

        if (isAvailable) {
            ReservationDTO reservation = new ReservationDTO();
            reservation.setTableId(tableId);
            reservation.setReservationId(restaurantId);
            reservation.setDinerIds(dinerIds);
            reservation.setReservationTime(reservationTime);
            reservation.setEndTime(endTime);

            repository.saveReservation(reservation);
            return reservation;
        } else {
            return null; // Time slot is not available
        }

    }
}
