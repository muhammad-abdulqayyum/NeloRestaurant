package com.example.nelorestaurant.dto;

import com.example.nelorestaurant.model.Diner;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ReservationDTO {

    private int reservationId;

    private int tableId;

    private Set<Integer> dinerIds = new HashSet<>();

    private LocalDateTime reservationTime;

    private LocalDateTime endTime;

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
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

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}
