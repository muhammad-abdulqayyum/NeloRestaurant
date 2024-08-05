package com.example.nelorestaurant.service;

import com.example.nelorestaurant.dao.NeloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NeloService {

    @Autowired
    NeloRepository repository;

    public boolean deleteReservation(int reservationId){
        return repository.deleteReservation(reservationId);
    }


}
