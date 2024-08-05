package com.example.nelorestaurant.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class NeloRepository {

    public static final String DELETE_RESERVATION_VIA_ID = "DELETE FROM reservations WHERE id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional
    public boolean deleteReservation(int reservationId){

        int rowsAffected = jdbcTemplate.update(DELETE_RESERVATION_VIA_ID, reservationId);

        if(rowsAffected > 0){
            return true; //Return true if at least one row was deleted
        } else {
            return false; //query delete error. todo: send specified message for processing error or 404 not found.

        }

    }

}
