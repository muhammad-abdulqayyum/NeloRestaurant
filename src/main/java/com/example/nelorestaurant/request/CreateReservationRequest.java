package com.example.nelorestaurant.request;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class CreateReservationRequest {

    private int restaurantId;
    private int tableId;
    private Set<Integer> dinerIds;
    private LocalDateTime reservationTime;
    private List<String> dietaryRestrictions;


    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public int getTableId() {
        return tableId;
    }

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public Set<Integer> getDinerIds() {
        return dinerIds;
    }

    public void setDinerIds(Set<Integer> dinerIds) {
        this.dinerIds = dinerIds;
    }

    public LocalDateTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(LocalDateTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    public List<String> getDietaryRestrictions() {
        return dietaryRestrictions;
    }

    public void setDietaryRestrictions(List<String> dietaryRestrictions) {
        this.dietaryRestrictions = dietaryRestrictions;
    }
}
