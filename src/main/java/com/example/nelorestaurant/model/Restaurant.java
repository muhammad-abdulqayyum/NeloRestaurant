package com.example.nelorestaurant.model;

import java.util.HashSet;
import java.util.Set;

public class Restaurant {

    private int restaurantId;
    private String restaurantName;
    private Set<String> endorsements = new HashSet<>();

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Set<String> getEndorsements() {
        return endorsements;
    }

    public void setEndorsements(Set<String> endorsements) {
        this.endorsements = endorsements;
    }

}
