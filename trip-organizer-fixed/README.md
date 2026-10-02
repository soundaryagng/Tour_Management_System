# ✈️ Trip Organizer - Complete Travel Booking System

A Spring Boot web application with Thymeleaf, H2 in-memory database, and a modern Bootstrap UI.

---

## 🚀 Quick Start

### Prerequisites
- **Java 17+** installed
- **Maven 3.6+** installed (or use the included wrapper)

### Run the Application

```bash
# Navigate to the project folder
cd trip-organizer

# Build and run
mvn spring-boot:run
```

The app starts at **http://localhost:8080**

---

## 🔑 Login Credentials

| Role     | Email                     | Password  |
|----------|---------------------------|-----------|
| Customer | test@email.com            | password  |
| Admin    | admin@travelease.com      | admin123  |

---

## 🗄️ H2 Database Console

Access the in-browser SQL console at:  
👉 **http://localhost:8080/h2-console**

| Field    | Value              |
|----------|--------------------|
| JDBC URL | `jdbc:h2:mem:tripdb` |
| Username | `sa`               |
| Password | *(leave empty)*    |

### Useful SQL Queries
```sql
-- View all trips
SELECT * FROM TRIPS;

-- View all users
SELECT * FROM USERS;

-- View all bookings with trip info
SELECT b.BOOKING_ID, u.NAME, t.PACKAGE_NAME, b.TRAVEL_DATE, b.TOTAL_AMOUNT, b.BOOKING_STATUS
FROM BOOKINGS b
JOIN USERS u ON b.USER_ID = u.ID
JOIN TRIPS t ON b.TRIP_ID = t.ID;

-- International trips only
SELECT * FROM TRIPS WHERE INTERNATIONAL = TRUE;

-- National trips only
SELECT * FROM TRIPS WHERE INTERNATIONAL = FALSE;
```

---

## 🌐 Application Pages

| URL                      | Description              |
|--------------------------|--------------------------|
| `/`                      | Home / Landing page      |
| `/trips`                 | All trip packages        |
| `/trips?type=international` | International packages |
| `/trips?type=national`   | National packages        |
| `/search?destination=X`  | Search trips             |
| `/login`                 | Login page               |
| `/register`              | Registration page        |
| `/dashboard`             | Customer dashboard       |
| `/my-bookings`           | User's bookings          |
| `/admin/dashboard`       | Admin panel              |
| `/h2-console`            | H2 database console      |

---

## 🏗️ Project Structure

```
src/main/java/com/travel/trip/
├── TripOrganizerApplication.java   # Main entry point
├── config/
│   ├── DataLoader.java             # Sample data initialization
│   └── SecurityConfig.java        # Spring Security config
├── controller/
│   ├── AuthController.java         # Login, register, dashboard
│   ├── BookingController.java      # Booking & payment
│   └── TripController.java         # Trip browsing & search
├── model/
│   ├── User.java
│   ├── Trip.java
│   └── Booking.java
├── repository/
│   ├── UserRepository.java
│   ├── TripRepository.java
│   └── BookingRepository.java
└── service/
    ├── AuthService.java
    ├── TripService.java
    └── BookingService.java

src/main/resources/
├── application.properties          # App & DB config
├── static/css/style.css            # Custom styles
└── templates/
    ├── index.html                  # Home page
    ├── login.html
    ├── register.html
    ├── trips.html                  # Trip listing
    ├── trip-details.html
    ├── booking.html
    ├── payment.html
    ├── dashboard.html
    ├── my-bookings.html
    └── admin-dashboard.html
```

---

## 🧰 Tech Stack

| Layer      | Technology                        |
|------------|-----------------------------------|
| Backend    | Spring Boot 3.1.5, Java 17        |
| Database   | H2 In-Memory (JPA / Hibernate)    |
| Frontend   | Thymeleaf + Bootstrap 5 + Font Awesome |
| Build      | Maven                             |
| Security   | Spring Security (permissive mode) |

---

## 📦 Build JAR

```bash
mvn clean package
java -jar target/trip-organizer-1.0.0.jar
```
