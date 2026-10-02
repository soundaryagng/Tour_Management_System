package com.travel.trip.controller;

import com.travel.trip.model.Booking;
import com.travel.trip.model.Trip;
import com.travel.trip.model.User;
import com.travel.trip.service.BookingService;
import com.travel.trip.service.TripService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TripService tripService;

    private boolean isAdmin(HttpSession session) {
        User user = (User) session.getAttribute("user");
        return user != null && "ADMIN".equals(user.getUserType());
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        List<Booking> allBookings = bookingService.getAllBookings();
        List<Trip> allTrips = tripService.getAllTrips();
        long confirmed = allBookings.stream().filter(b -> "CONFIRMED".equals(b.getBookingStatus())).count();
        long cancelled = allBookings.stream().filter(b -> "CANCELLED".equals(b.getBookingStatus())).count();
        double revenue = allBookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getBookingStatus()))
                .mapToDouble(Booking::getTotalAmount).sum();
        model.addAttribute("totalBookings", allBookings.size());
        model.addAttribute("confirmedBookings", confirmed);
        model.addAttribute("cancelledBookings", cancelled);
        model.addAttribute("totalRevenue", revenue);
        model.addAttribute("totalTrips", allTrips.size());
        model.addAttribute("recentBookings", allBookings.stream().limit(10).toList());
        return "admin-dashboard";
    }

    @GetMapping("/trips")
    public String manageTrips(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("trips", tripService.getAllTrips());
        return "admin-trips";
    }

    @GetMapping("/trips/add")
    public String addTripPage(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        return "admin-trip-form";
    }

    @PostMapping("/trips/add")
    public String addTrip(HttpSession session,
                          @RequestParam String packageId,
                          @RequestParam String packageName,
                          @RequestParam String destination,
                          @RequestParam String country,
                          @RequestParam int durationDays,
                          @RequestParam double price,
                          @RequestParam String description,
                          @RequestParam(defaultValue = "") String imageUrl,
                          @RequestParam boolean international,
                          @RequestParam double rating,
                          @RequestParam int totalSeats,
                          @RequestParam String inclusions,
                          @RequestParam String exclusions,
                          Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        // Use fallback image if empty
        if (imageUrl == null || imageUrl.trim().isEmpty()) {
            imageUrl = international
                ? "https://images.unsplash.com/photo-1476514525535-07fb3b4ae5f1?w=600&q=80"
                : "https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=600&q=80";
        }
        Trip trip = new Trip(packageId, packageName, destination, country, durationDays, price,
                description, imageUrl, international, rating, totalSeats, inclusions, exclusions);
        tripService.saveTrip(trip);
        return "redirect:/admin/trips?success=Trip+added+successfully";
    }

    @GetMapping("/trips/edit/{id}")
    public String editTripPage(@PathVariable Long id, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        Trip trip = tripService.getTripById(id);
        if (trip == null) return "redirect:/admin/trips";
        model.addAttribute("trip", trip);
        return "admin-trip-edit";
    }

    @PostMapping("/trips/edit/{id}")
    public String editTrip(@PathVariable Long id, HttpSession session,
                           @RequestParam String packageName,
                           @RequestParam String destination,
                           @RequestParam String country,
                           @RequestParam int durationDays,
                           @RequestParam double price,
                           @RequestParam String description,
                           @RequestParam(defaultValue = "") String imageUrl,
                           @RequestParam boolean international,
                           @RequestParam double rating,
                           @RequestParam int totalSeats,
                           @RequestParam String inclusions,
                           @RequestParam String exclusions) {
        if (!isAdmin(session)) return "redirect:/login";
        Trip trip = tripService.getTripById(id);
        if (trip != null) {
            trip.setPackageName(packageName);
            trip.setDestination(destination);
            trip.setCountry(country);
            trip.setDurationDays(durationDays);
            trip.setPrice(price);
            trip.setDescription(description);
            if (imageUrl != null && !imageUrl.trim().isEmpty()) trip.setImageUrl(imageUrl.trim());
            trip.setInternational(international);
            trip.setRating(rating);
            trip.setTotalSeats(totalSeats);
            trip.setInclusions(inclusions);
            trip.setExclusions(exclusions);
            tripService.saveTrip(trip);
        }
        return "redirect:/admin/trips?success=Trip+updated";
    }

    @PostMapping("/trips/delete/{id}")
    public String deleteTrip(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        tripService.deleteTrip(id);
        return "redirect:/admin/trips?success=Trip+deleted";
    }

    @GetMapping("/bookings")
    public String manageBookings(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/login";
        model.addAttribute("bookings", bookingService.getAllBookings());
        return "admin-bookings";
    }

    @PostMapping("/bookings/cancel/{id}")
    public String cancelBooking(@PathVariable Long id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/login";
        bookingService.cancelBooking(id);
        return "redirect:/admin/bookings?success=Booking+cancelled";
    }
}
