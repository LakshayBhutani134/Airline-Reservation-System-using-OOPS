package com.example.AirlineReservationSystem.controller;

import com.example.AirlineReservationSystem.Util.BadRequest;
import com.example.AirlineReservationSystem.Util.ExceptionHandle;
import com.example.AirlineReservationSystem.Util.Response;
import com.example.AirlineReservationSystem.service.ReservationService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import jakarta.transaction.Transactional;

@Transactional(rollbackOn = {IOException.class, SQLException.class})
@RestController
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    @RequestMapping(value="/reservation/{number}", method= RequestMethod.GET, produces={"application/json", "application/xml"})
    public ResponseEntity<?> getReservation(
            @PathVariable String number,
            @RequestParam(value="xml", required = false)       String xml
    ) {
    	try {
    		return reservationService.getReservation(number);
    	}catch (NotFoundException e) {
    		return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404, e.getMessage())));
		}
    }
    
    @RequestMapping(value="/reservation", method=RequestMethod.POST, produces={"application/json", "application/xml"})
 	public ResponseEntity<?> createReservation(
 			@RequestParam("passengerId") String passengerId,
 			@RequestParam("flightNumbers") List<String> flightNumbers,
 			@RequestParam(value = "xml", required=false) String xml
 			)
 			 {
 		
    	try {
			return reservationService.createReservation(passengerId, flightNumbers);
		} catch (IllegalArgumentException e) {
			return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400, e.getMessage())));
		}
 		
 	}
    
    @RequestMapping(value="/reservation/{number}", method=RequestMethod.POST, produces={"application/json", "application/xml"})
   	public ResponseEntity<?> updateReservation(
   			@PathVariable String number,
   			@RequestParam(required=false) List<String> flightsAdded,
   			@RequestParam(required=false) List<String> flightsRemoved,
   			@RequestParam(value = "xml", required=false) String xml
   			)
   			 {
    	try {
			return reservationService.updateReservation(number, flightsAdded,flightsRemoved);
    	}catch (IllegalArgumentException e) {
    		return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(400, e.getMessage())));
		}catch (NotFoundException e) {
			return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404, e.getMessage())));
		}
   	}
    
    @RequestMapping(value="/reservation/{number}", method=RequestMethod.DELETE, produces={"application/json", "application/xml"})
   	public ResponseEntity<?> cancelReservation(
   			@PathVariable String number,
   			@RequestParam(value = "xml", required=false) String xml
   			)
   			 {
    	try {
    		reservationService.cancelReservation(number);
    		return ResponseEntity.status(HttpStatus.OK).body(new Response(200,"Reservation with number " + number + " is cancelled successfully "));
    	}catch (NotFoundException e) {
    		return ResponseEntity.badRequest().body(new ExceptionHandle(new BadRequest(404, e.getMessage())));
		}
	
   	}
}