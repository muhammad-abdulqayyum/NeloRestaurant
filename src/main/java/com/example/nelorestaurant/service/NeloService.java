package com.example.nelorestaurant.service;

import com.example.nelorestaurant.dao.NeloRepository;
import com.example.nelorestaurant.model.Restaurant;
import com.example.nelorestaurant.model.Table;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class NeloService {

    @Autowired
    NeloRepository repository;

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

    public List<Table> getOpenTables(int groupSize, LocalDateTime time) {

        List<Table> openTables = repository.getOpenTablesFromDB(groupSize, time);

        List<Restaurant> allRestaurants = getAllRestaurants();
        HashMap<Integer, Restaurant> restaurantMap = new HashMap<>();

        //Create RestaurantMap / Dictionary
        for (Restaurant restaurant: allRestaurants){
            int restaurantId = restaurant.getRestaurantId();
            restaurantMap.put(restaurantId, restaurant);
        }

        //Fill in Restaurant Data
        for(Table table : openTables){

            int restaurantId = table.getRestaurant().getRestaurantId();
            Restaurant restaurant = restaurantMap.get(restaurantId);
            table.setRestaurant(restaurant);
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


}
