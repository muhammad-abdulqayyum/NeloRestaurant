package com.example.nelorestaurant.dao;

import com.example.nelorestaurant.model.Restaurant;
import com.example.nelorestaurant.model.Table;
import com.example.nelorestaurant.model.TableDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class NeloRepository {

    //Logger
    private static final Logger log = LoggerFactory.getLogger(NeloRepository.class);

    //SQL Query Constants
    public static final String GET_OPEN_TABLES = "SELECT * FROM tables capacity >= ?";

    public static final String GET_ALL_RESTAURANTS = "SELECT * FROM restaurants";
    public static final String DELETE_RESERVATION_VIA_ID = "DELETE FROM reservations WHERE id = ?";

    @Autowired
    JdbcTemplate jdbcTemplate;

    //Row Mappers
    private static final RowMapper<Restaurant> RESTAURANT_ROW_MAPPER = new RowMapper<>() {
        @Override
        public Restaurant mapRow(ResultSet rs, int rowNum) throws SQLException {
            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantId(rs.getInt("id"));
            restaurant.setRestaurantName(rs.getString("name"));

            String endorsementDetails = rs.getString("endorsements");
            String [] endorsements = endorsementDetails.split(",");

            List<String> endorsementList = new ArrayList<>(List.of(endorsements));
            endorsementList.replaceAll(String::toUpperCase);
            Set<String> endorsementSet = new HashSet<>(endorsementList);

            restaurant.setEndorsements(endorsementSet);

            log.debug("retrieved restaurant from DB: {}", restaurant);

            return restaurant;
        }
    };

    public static final RowMapper<TableDTO> TABLE_ROW_MAPPER = new RowMapper<>() {
        @Override
        public TableDTO mapRow(ResultSet rs, int rowNum) throws SQLException {

            TableDTO tableDTO = new TableDTO();

            tableDTO.setTableId(rs.getInt("id"));
            tableDTO.setTableCapacity(rs.getInt("capacity"));
            tableDTO.setRestaurantId(rs.getInt("restaurant_id"));

            return tableDTO;
        }
    };

    //DAO Methods

    public List<Table> getOpenTablesFromDB(int groupSize, LocalDateTime time){
        List<TableDTO> openTableFromDB = jdbcTemplate.query(GET_OPEN_TABLES, TABLE_ROW_MAPPER);

        List<Table> openTables = new ArrayList<>();
        //todo: set openTables with data from openTablesFromDB

        for(TableDTO tableDTO: openTableFromDB){
            Table table = new Table();
            table.setTableId(tableDTO.getTableId());
            table.setTableCapacity(tableDTO.getTableCapacity());

            Restaurant restaurant = new Restaurant();
            restaurant.setRestaurantId(tableDTO.getRestaurantId());

            table.setRestaurant(restaurant);
        }

        return openTables;
    }


    /**
     *
     * @return List of all Restaurants
     */
    public List<Restaurant> getAllRestaurants() {

        List<Restaurant> restaurants = jdbcTemplate.query(GET_ALL_RESTAURANTS,RESTAURANT_ROW_MAPPER);

        return restaurants;
    }

    /**
     *
     * Deletes reservation from DB based on reservationId param
     *
     * @param reservationId
     * @return
     */
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
