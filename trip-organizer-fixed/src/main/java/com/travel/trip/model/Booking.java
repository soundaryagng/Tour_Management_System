package com.travel.trip.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bookingId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    private LocalDate travelDate;
    private int numberOfAdults;
    private int numberOfChildren;
    private int numberOfTravelers;
    private double totalAmount;
    private String bookingStatus;
    private LocalDateTime bookingDate;
    private String paymentStatus;
    private String cancellationDeadline;
    private String paymentMethod;
    private String upiId;

    public Booking() {
        this.bookingDate = LocalDateTime.now();
        this.bookingStatus = "PENDING";
        this.paymentStatus = "PENDING";
    }

    public Booking(String bookingId, User user, Trip trip, LocalDate travelDate,
                   int numberOfAdults, int numberOfChildren, double totalAmount) {
        this.bookingId = bookingId;
        this.user = user;
        this.trip = trip;
        this.travelDate = travelDate;
        this.numberOfAdults = numberOfAdults;
        this.numberOfChildren = numberOfChildren;
        this.numberOfTravelers = numberOfAdults + numberOfChildren;
        this.totalAmount = totalAmount;
        this.bookingDate = LocalDateTime.now();
        this.bookingStatus = "PENDING";
        this.paymentStatus = "PENDING";
        this.cancellationDeadline = travelDate.minusDays(7).toString();
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getBookingId() { return bookingId; }
    public void setBookingId(String bookingId) { this.bookingId = bookingId; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Trip getTrip() { return trip; }
    public void setTrip(Trip trip) { this.trip = trip; }
    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
    public int getNumberOfAdults() { return numberOfAdults; }
    public void setNumberOfAdults(int numberOfAdults) { this.numberOfAdults = numberOfAdults; }
    public int getNumberOfChildren() { return numberOfChildren; }
    public void setNumberOfChildren(int numberOfChildren) { this.numberOfChildren = numberOfChildren; }
    public int getNumberOfTravelers() { return numberOfTravelers; }
    public void setNumberOfTravelers(int numberOfTravelers) { this.numberOfTravelers = numberOfTravelers; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public String getBookingStatus() { return bookingStatus; }
    public void setBookingStatus(String bookingStatus) { this.bookingStatus = bookingStatus; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public void setBookingDate(LocalDateTime bookingDate) { this.bookingDate = bookingDate; }
    public String getPaymentStatus() { return paymentStatus; }
    public void setPaymentStatus(String paymentStatus) { this.paymentStatus = paymentStatus; }
    public String getCancellationDeadline() { return cancellationDeadline; }
    public void setCancellationDeadline(String cancellationDeadline) { this.cancellationDeadline = cancellationDeadline; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getUpiId() { return upiId; }
    public void setUpiId(String upiId) { this.upiId = upiId; }

    public void confirmBooking() {
        this.bookingStatus = "CONFIRMED";
        this.paymentStatus = "PAID";
        trip.bookSeats(numberOfTravelers);
    }

    public void cancelBooking() {
        this.bookingStatus = "CANCELLED";
        this.paymentStatus = "REFUNDED";
    }
}
