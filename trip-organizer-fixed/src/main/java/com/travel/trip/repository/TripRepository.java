package com.travel.trip.repository;

import com.travel.trip.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {
    List<Trip> findByInternational(boolean international);
    List<Trip> findByDestinationContainingIgnoreCase(String destination);
    List<Trip> findByPriceBetween(double minPrice, double maxPrice);
    Optional<Trip> findByPackageId(String packageId);
}
