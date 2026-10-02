package com.travel.trip.model;

import jakarta.persistence.*;

@Entity
@Table(name = "trips")
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String packageId;
    private String packageName;
    private String destination;
    private String country;
    private int durationDays;
    private double price;

    @Column(length = 1000)
    private String description;

    private String imageUrl;
    private boolean international;
    private double rating;
    private int availableSeats;
    private int totalSeats;

    @Column(length = 500)
    private String inclusions;

    @Column(length = 500)
    private String exclusions;

    public Trip() {}

    public Trip(String packageId, String packageName, String destination, String country,
                int durationDays, double price, String description, String imageUrl,
                boolean international, double rating, int totalSeats,
                String inclusions, String exclusions) {
        this.packageId = packageId;
        this.packageName = packageName;
        this.destination = destination;
        this.country = country;
        this.durationDays = durationDays;
        this.price = price;
        this.description = description;
        this.imageUrl = imageUrl;
        this.international = international;
        this.rating = rating;
        this.totalSeats = totalSeats;
        this.availableSeats = totalSeats;
        this.inclusions = inclusions;
        this.exclusions = exclusions;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getPackageId() { return packageId; }
    public void setPackageId(String packageId) { this.packageId = packageId; }
    public String getPackageName() { return packageName; }
    public void setPackageName(String packageName) { this.packageName = packageName; }
    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
    public int getDurationDays() { return durationDays; }
    public void setDurationDays(int durationDays) { this.durationDays = durationDays; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isInternational() { return international; }
    public void setInternational(boolean international) { this.international = international; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
    public int getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(int availableSeats) { this.availableSeats = availableSeats; }
    public int getTotalSeats() { return totalSeats; }
    public void setTotalSeats(int totalSeats) { this.totalSeats = totalSeats; }
    public String getInclusions() { return inclusions; }
    public void setInclusions(String inclusions) { this.inclusions = inclusions; }
    public String getExclusions() { return exclusions; }
    public void setExclusions(String exclusions) { this.exclusions = exclusions; }

    public boolean bookSeats(int seats) {
        if (seats <= availableSeats) {
            availableSeats -= seats;
            return true;
        }
        return false;
    }
}
