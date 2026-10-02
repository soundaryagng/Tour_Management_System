package com.travel.trip.controller;

import com.travel.trip.model.Trip;
import com.travel.trip.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
public class TripController {

    @Autowired
    private TripService tripService;

    @GetMapping("/trips")
    public String viewTrips(@RequestParam(required = false) String type, Model model) {
        List<Trip> trips;
        if ("international".equals(type)) {
            trips = tripService.getInternationalTrips();
            model.addAttribute("title", "International Trips");
            model.addAttribute("subtitle", "Explore the world beyond borders");
        } else if ("national".equals(type)) {
            trips = tripService.getNationalTrips();
            model.addAttribute("title", "National Trips");
            model.addAttribute("subtitle", "Discover the beauty of India");
        } else {
            trips = tripService.getAllTrips();
            model.addAttribute("title", "All Trips");
            model.addAttribute("subtitle", "Browse all available packages");
        }
        model.addAttribute("trips", trips);
        return "trips";
    }

    @GetMapping("/trip/{id}")
    public String tripDetails(@PathVariable Long id, Model model) {
        Trip trip = tripService.getTripById(id);
        if (trip == null) return "redirect:/trips";
        model.addAttribute("trip", trip);
        return "trip-details";
    }

    @GetMapping("/search")
    public String searchTrips(@RequestParam String destination, Model model) {
        List<Trip> trips = tripService.searchByDestination(destination);
        model.addAttribute("trips", trips);
        model.addAttribute("title", "Search Results");
        model.addAttribute("subtitle", "Results for: " + destination);
        return "trips";
    }

    // JSON API used by the Voyager chatbot widget to read live package data
    @GetMapping("/api/trips")
    @ResponseBody
    public List<Trip> getTripsJson() {
        return tripService.getAllTrips();
    }
}
