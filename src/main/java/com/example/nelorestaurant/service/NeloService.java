package com.example.nelorestaurant.service;

import com.example.nelorestaurant.dao.NeloDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NeloService {

    @Autowired
    NeloDAO repository;

}
