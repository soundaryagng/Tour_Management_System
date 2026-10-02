package com.travel.trip.service;

import com.travel.trip.model.Booking;
import com.travel.trip.model.Trip;
import com.travel.trip.model.User;
import com.travel.trip.repository.BookingRepository;
import com.travel.trip.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private TripService tripService;

    @Autowired
    private TripRepository tripRepository;

    public Booking createBooking(User user, Long tripId, LocalDate travelDate, int adults, int children) {
        Trip trip = tripService.getTripById(tripId);
        int totalTravelers = adults + children;
        if (trip == null || trip.getAvailableSeats() < totalTravelers) {
            return null;
        }
        // Children get 50% discount
        double totalAmount = (trip.getPrice() * adults) + (trip.getPrice() * 0.5 * children);
        String bookingId = "BKG" + System.currentTimeMillis();
        Booking booking = new Booking(bookingId, user, trip, travelDate, adults, children, totalAmount);
        return bookingRepository.save(booking);
    }

    public Booking confirmBooking(Long bookingId, String paymentMethod, String upiId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            Trip trip = booking.getTrip();
            trip.bookSeats(booking.getNumberOfTravelers());
            tripRepository.save(trip);

            booking.setBookingStatus("CONFIRMED");
            booking.setPaymentStatus("PAID");
            booking.setPaymentMethod(paymentMethod);
            if (upiId != null && !upiId.trim().isEmpty()) {
                booking.setUpiId(upiId.trim());
            }
            return bookingRepository.save(booking);
        }
        return null;
    }

    public Booking cancelBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            if ("CONFIRMED".equals(booking.getBookingStatus())) {
                Trip trip = booking.getTrip();
                trip.setAvailableSeats(trip.getAvailableSeats() + booking.getNumberOfTravelers());
                tripRepository.save(trip);
            }
            booking.setBookingStatus("CANCELLED");
            booking.setPaymentStatus("REFUNDED");
            return bookingRepository.save(booking);
        }
        return null;
    }

    public List<Booking> getUserBookings(User user) {
        return bookingRepository.findByUserOrderByBookingDateDesc(user);
    }

    public Booking getBookingById(Long id) {
        return bookingRepository.findById(id).orElse(null);
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }
}
