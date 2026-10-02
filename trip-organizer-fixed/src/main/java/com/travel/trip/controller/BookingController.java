package com.travel.trip.controller;

import com.travel.trip.model.Booking;
import com.travel.trip.model.User;
import com.travel.trip.service.BookingService;
import com.travel.trip.service.TripService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@Controller
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TripService tripService;

    @GetMapping("/book/{tripId}")
    public String bookPage(@PathVariable Long tripId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("trip", tripService.getTripById(tripId));
        model.addAttribute("minDate", LocalDate.now().plusDays(1).toString());
        return "booking";
    }

    @PostMapping("/book")
    public String createBooking(@RequestParam Long tripId,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate travelDate,
                                @RequestParam(defaultValue = "1") int adults,
                                @RequestParam(defaultValue = "0") int children,
                                HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        if (adults < 1) {
            model.addAttribute("error", "At least 1 adult is required.");
            model.addAttribute("trip", tripService.getTripById(tripId));
            model.addAttribute("minDate", LocalDate.now().plusDays(1).toString());
            return "booking";
        }
        Booking booking = bookingService.createBooking(user, tripId, travelDate, adults, children);
        if (booking == null) {
            model.addAttribute("error", "Not enough seats available or invalid trip.");
            model.addAttribute("trip", tripService.getTripById(tripId));
            model.addAttribute("minDate", LocalDate.now().plusDays(1).toString());
            return "booking";
        }
        return "redirect:/payment/" + booking.getId();
    }

    @GetMapping("/payment/{bookingId}")
    public String paymentPage(@PathVariable Long bookingId, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        Booking booking = bookingService.getBookingById(bookingId);
        if (booking == null) return "redirect:/my-bookings";
        model.addAttribute("booking", booking);
        return "payment";
    }

    @PostMapping("/payment/confirm")
    public String confirmPayment(@RequestParam Long bookingId,
                                 @RequestParam String paymentMethod,
                                 @RequestParam(required = false) String upiId,
                                 HttpSession session, Model model) {
        Booking booking = bookingService.confirmBooking(bookingId, paymentMethod, upiId);
        if (booking == null) return "redirect:/my-bookings";
        model.addAttribute("booking", booking);
        return "receipt";
    }

    @GetMapping("/my-bookings")
    public String myBookings(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        model.addAttribute("bookings", bookingService.getUserBookings(user));
        return "my-bookings";
    }

    @PostMapping("/cancel-booking")
    public String cancelBooking(@RequestParam Long bookingId, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        bookingService.cancelBooking(bookingId);
        return "redirect:/my-bookings";
    }
}
