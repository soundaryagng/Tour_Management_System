package com.travel.trip.service;

import com.travel.trip.model.Trip;
import com.travel.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TripService {

    @Autowired
    private TripRepository tripRepository;

    public List<Trip> getAllTrips() {
        return tripRepository.findAll();
    }

    public List<Trip> getInternationalTrips() {
        return tripRepository.findByInternational(true);
    }

    public List<Trip> getNationalTrips() {
        return tripRepository.findByInternational(false);
    }

    public Trip getTripById(Long id) {
        return tripRepository.findById(id).orElse(null);
    }

    public List<Trip> searchByDestination(String destination) {
        return tripRepository.findByDestinationContainingIgnoreCase(destination);
    }

    public Trip saveTrip(Trip trip) {
        return tripRepository.save(trip);
    }

    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);
    }
}
