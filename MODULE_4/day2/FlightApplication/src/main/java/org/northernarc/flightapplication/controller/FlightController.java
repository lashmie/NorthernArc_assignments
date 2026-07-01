package org.northernarc.flightapplication.controller;

import org.northernarc.flightapplication.dto.FlightRequestDTO;
import org.northernarc.flightapplication.dto.FlightResponseDTO;
import org.northernarc.flightapplication.service.FlightService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/flights")
public class FlightController {
    @Autowired
    private FlightService flightService;

    @PostMapping("/create")
    public ResponseEntity<FlightResponseDTO> creatFlight(@RequestBody FlightRequestDTO flightRequestDTO){
        FlightResponseDTO createdFlight = flightService.createFlight(flightRequestDTO);
        return ResponseEntity.ok(createdFlight);
    }
    @GetMapping("/get/{flightId}")
    public ResponseEntity<FlightResponseDTO> getFlightById(@PathVariable Long flightId){
        FlightResponseDTO flightResponseDTO = flightService.findFlightById(flightId);
        return ResponseEntity.ok(flightResponseDTO);}
    @GetMapping("/getall")
    public ResponseEntity<List<FlightResponseDTO>> getAllFlights(){
        List<FlightResponseDTO> flights = flightService.findAll();
        return ResponseEntity.ok(flights);
    }
//    @PutMapping("/update")//impl not created
//    public ResponseEntity<FlightResponseDTO> updateFlight(@RequestBody FlightRequestDTO flightRequestDTO){
//        FlightResponseDTO updatedFlight = flightService.update(flightRequestDTO);
//        return ResponseEntity.ok(updatedFlight);
//    }
@DeleteMapping("/delete/{id}")
public ResponseEntity<String> deleteFlight(@PathVariable Long id){
    flightService.deleteFlight(id);
    return ResponseEntity.ok("Flight deleted successfully");
}
}
