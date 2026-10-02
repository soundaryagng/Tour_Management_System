package com.travel.trip.config;

import com.travel.trip.model.Trip;
import com.travel.trip.model.User;
import com.travel.trip.repository.TripRepository;
import com.travel.trip.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Admin User
        if (!userRepository.existsByEmail("admin@travelease.com")) {
            userRepository.save(new User("admin@travelease.com", "admin123",
                    "System Admin", "9999999999", "ADMIN"));
        }
        // Demo Customer
        if (!userRepository.existsByEmail("test@email.com")) {
            userRepository.save(new User("test@email.com", "password",
                    "Demo User", "1234567890", "CUSTOMER"));
        }

        // ===== INTERNATIONAL TRIPS (15 total) =====
        Trip[] internationalTrips = {
            new Trip("I001", "Swiss Alps Adventure", "Interlaken", "Switzerland",
                    10, 2500.0,
                    "Breathtaking mountain adventure in the Swiss Alps with skiing, trekking and scenic train rides. Experience snow-capped peaks, crystal-clear lakes and charming Alpine villages.",
                    "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=600&q=80",
                    true, 4.8, 20,
                    "Flights, 4-star hotel, ski passes, trekking guide, breakfast included",
                    "Personal expenses, tips, travel insurance"),

            new Trip("I002", "Bali Paradise", "Bali", "Indonesia",
                    7, 1800.0,
                    "Beautiful beaches, lush rice terraces, vibrant temples and world-class resorts. Immerse yourself in Balinese culture, yoga retreats and stunning sunsets.",
                    "https://images.unsplash.com/photo-1537996194471-e657df975ab4?w=600&q=80",
                    true, 4.7, 25,
                    "Flights, villa accommodation, temple tours, rice terrace trek, daily breakfast",
                    "Personal shopping, alcohol, optional activities"),

            new Trip("I003", "Japan Cultural Tour", "Tokyo", "Japan",
                    8, 2200.0,
                    "Experience ancient shrines, futuristic cities, cherry blossoms and authentic cuisine. Visit Tokyo, Kyoto, Osaka and the iconic Mount Fuji.",
                    "https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?w=600&q=80",
                    true, 4.9, 15,
                    "Flights, JR Rail Pass, hotels, guided city tours, sake tasting",
                    "Personal meals, museum entry fees not listed, tips"),

            new Trip("I004", "Paris Highlights", "Paris", "France",
                    6, 2100.0,
                    "Romance, fine dining, art museums and iconic landmarks in the City of Light. Visit the Eiffel Tower, Louvre, Montmartre and enjoy French cuisine.",
                    "https://images.unsplash.com/photo-1499856871958-5b9357976b82?w=600&q=80",
                    true, 4.6, 30,
                    "Flights, 4-star hotel near Champs-Elysees, Seine river cruise, Eiffel Tower tickets",
                    "Personal meals, shopping, optional excursions"),

            new Trip("I005", "Thailand Beaches", "Phuket", "Thailand",
                    5, 1200.0,
                    "Crystal-clear waters, island hopping and vibrant nightlife in a tropical paradise. Explore Phi Phi Islands, Phang Nga Bay and Patong Beach.",
                    "https://images.unsplash.com/photo-1552465011-b4e21bf6e79a?w=600&q=80",
                    true, 4.5, 40,
                    "Flights, beach resort, island hopping tour, snorkeling gear, transfers",
                    "Personal expenses, optional water sports, alcohol"),

            new Trip("I006", "Dubai Extravaganza", "Dubai", "UAE",
                    5, 1900.0,
                    "Luxury shopping, towering skyscrapers, desert safaris and world-class entertainment. Visit the Burj Khalifa, Palm Jumeirah and experience a thrilling desert camp.",
                    "https://images.unsplash.com/photo-1512453979798-5ea266f8880c?w=600&q=80",
                    true, 4.7, 20,
                    "Flights, 5-star hotel, Burj Khalifa tickets, desert safari, dhow cruise dinner",
                    "Personal shopping, spa, extra excursions"),

            new Trip("I007", "Maldives Escape", "Malé", "Maldives",
                    6, 3500.0,
                    "Overwater bungalows, pristine coral reefs and endless turquoise lagoons. The perfect honeymoon and luxury getaway destination.",
                    "https://images.unsplash.com/photo-1514282401047-d79a71a590e8?w=600&q=80",
                    true, 5.0, 10,
                    "Seaplane transfers, overwater villa, all meals, snorkeling & diving, sunset cruise",
                    "Spa treatments, personal purchases"),

            new Trip("I008", "Singapore City Tour", "Singapore", "Singapore",
                    4, 1500.0,
                    "Explore the Lion City's stunning blend of cultures, futuristic architecture and street food paradise. Visit Gardens by the Bay, Sentosa and Marina Bay Sands.",
                    "https://images.unsplash.com/photo-1525625293386-3f8f99389edd?w=600&q=80",
                    true, 4.6, 30,
                    "Flights, hotel in Marina Bay, city tour, Gardens by the Bay, cable car ride",
                    "Personal meals, Universal Studios, shopping"),

            new Trip("I009", "New York City Break", "New York", "USA",
                    7, 2800.0,
                    "The Big Apple awaits! Times Square, Central Park, Statue of Liberty, Broadway shows and legendary NYC pizza. An unforgettable urban adventure.",
                    "https://images.unsplash.com/photo-1534430480872-3498386e7856?w=600&q=80",
                    true, 4.5, 25,
                    "Flights, Midtown hotel, Statue of Liberty ferry, Empire State Building, city pass",
                    "Broadway tickets, personal dining, shopping"),

            new Trip("I010", "Greece Island Hopping", "Athens", "Greece",
                    9, 2400.0,
                    "Explore the cradle of civilization. Visit Athens, Santorini's iconic blue domes and Mykonos' golden beaches. Ancient history meets stunning beauty.",
                    "https://images.unsplash.com/photo-1533104816931-20fa691ff6ca?w=600&q=80",
                    true, 4.8, 20,
                    "Flights, island ferry passes, hotels, Acropolis guided tour, sunset dinner in Santorini",
                    "Personal shopping, optional water activities, tips"),

            // 5 NEW INTERNATIONAL TRIPS
            new Trip("I011", "Machu Picchu Explorer", "Cusco", "Peru",
                    10, 2700.0,
                    "Trek the legendary Inca Trail to the ancient citadel of Machu Picchu hidden in the Andes. Explore Cusco's colonial streets, Sacred Valley and traditional Andean villages.",
                    "https://images.unsplash.com/photo-1526392060635-9d6019884377?w=600&q=80",
                    true, 4.8, 18,
                    "Flights, Inca Trail permits, guided trek, train to Aguas Calientes, hotel, Sacred Valley tour",
                    "Personal gear, altitude sickness medication, tips"),

            new Trip("I012", "Safari Kenya Adventure", "Nairobi", "Kenya",
                    8, 3200.0,
                    "Witness the Great Migration in the Masai Mara, spot the Big Five on game drives, and experience authentic Maasai culture under the vast African sky.",
                    "https://images.unsplash.com/photo-1516426122078-c23e76319801?w=600&q=80",
                    true, 4.9, 12,
                    "Flights, safari lodge, all meals, daily game drives, Masai Mara park fees, Nairobi city tour",
                    "Visa fees, personal shopping, optional balloon safari"),

            new Trip("I013", "Barcelona & Costa Brava", "Barcelona", "Spain",
                    7, 1950.0,
                    "Experience Gaudi's masterpieces, tapas culture, sun-soaked beaches and the vibrant nightlife of Catalunya. Day trip to the stunning Costa Brava cliffs.",
                    "https://images.unsplash.com/photo-1539037116277-4db20889f2d4?w=600&q=80",
                    true, 4.7, 28,
                    "Flights, boutique hotel in Gothic Quarter, Sagrada Familia, Park Guell, wine & tapas tour, Costa Brava day trip",
                    "Personal meals, clubs, optional day trips"),

            new Trip("I014", "Vietnam Complete Tour", "Hanoi", "Vietnam",
                    12, 1600.0,
                    "Travel from North to South: Ha Long Bay cruise, Hoi An lantern streets, Ho Chi Minh City's history, and the Mekong Delta. Vietnam's full spectrum of beauty.",
                    "https://images.unsplash.com/photo-1528360983277-13d401cdc186?w=600&q=80",
                    true, 4.7, 22,
                    "Flights, Ha Long Bay overnight cruise, hotels throughout, guided city tours, street food walk",
                    "Personal meals outside itinerary, tips, optional motorbike tour"),

            new Trip("I015", "Iceland Northern Lights", "Reykjavik", "Iceland",
                    6, 3100.0,
                    "Chase the Aurora Borealis across Iceland's otherworldly landscapes. Explore geysers, waterfalls, black sand beaches, the Golden Circle and soak in the Blue Lagoon.",
                    "https://images.unsplash.com/photo-1531366936337-7c912a4589a7?w=600&q=80",
                    true, 4.9, 15,
                    "Flights, hotel in Reykjavik, Golden Circle tour, Blue Lagoon entry, Northern Lights hunt tour, Gullfoss waterfall",
                    "Personal meals, optional snowmobile, tips"),
        };

        // ===== NATIONAL TRIPS (15 total) =====
        Trip[] nationalTrips = {
            new Trip("N001", "Kerala Backwaters", "Alleppey", "India",
                    5, 500.0,
                    "Houseboat cruise through tranquil backwaters of God's Own Country. Experience coconut groves, village life, fresh seafood and lush paddy fields.",
                    "https://images.unsplash.com/photo-1602216056096-3b40cc0c9944?w=600&q=80",
                    false, 4.7, 30,
                    "Houseboat stay, all Kerala meals, Kathakali show, spice plantation visit, transfers",
                    "Personal drinks, tips, flight to Cochin"),

            new Trip("N002", "Goa Beach Holiday", "Panaji", "India",
                    4, 400.0,
                    "Sun, sand, sea and soulful sunsets on India's most famous coastline. Enjoy water sports, Portuguese heritage churches, beach shacks and vibrant nightlife.",
                    "https://images.unsplash.com/photo-1588416936097-41850ab3d86d?w=600&q=80",
                    false, 4.6, 35,
                    "Beach resort, breakfast & dinner, water sports package, Old Goa church tour",
                    "Nightclub entry, personal shopping, alcohol"),

            new Trip("N003", "Manali Adventure", "Manali", "India",
                    6, 350.0,
                    "Himalayan adventure with snow, rivers, Rohtang Pass and river rafting in the Beas. Perfect for thrill-seekers and nature lovers.",
                    "https://images.unsplash.com/photo-1626621341517-bbf3d9990a23?w=600&q=80",
                    false, 4.5, 25,
                    "Hotel stays, Rohtang Pass excursion, river rafting, Solang Valley, all transfers",
                    "Personal meals, optional trekking gear, tips"),

            new Trip("N004", "Jaipur Heritage", "Jaipur", "India",
                    4, 300.0,
                    "Royal palaces, historic forts and the rich culture of the Pink City. Explore Amber Fort, City Palace, Hawa Mahal and local bazaars.",
                    "https://images.unsplash.com/photo-1477587458883-47145ed94245?w=600&q=80",
                    false, 4.6, 20,
                    "Heritage hotel, daily breakfast, Amber Fort guided tour, elephant ride, pink city walk",
                    "Shopping, personal meals, tips"),

            new Trip("N005", "Agra Taj Mahal", "Agra", "India",
                    3, 250.0,
                    "Witness the iconic Taj Mahal at sunrise—a true wonder of the world. Also visit the majestic Agra Fort and Fatehpur Sikri.",
                    "https://images.unsplash.com/photo-1564507592333-c60657eea523?w=600&q=80",
                    false, 4.8, 25,
                    "Hotel, Taj Mahal at sunrise, Agra Fort, Fatehpur Sikri, guided tour, transfers",
                    "Personal meals not listed, tips, shopping"),

            new Trip("N006", "Ladakh Expedition", "Leh", "India",
                    7, 650.0,
                    "High-altitude adventure in the land of passes. Pangong Lake, Nubra Valley, ancient monasteries and a motorcycle ride on the world's highest motorable roads.",
                    "https://images.unsplash.com/photo-1598091383021-15ddea10925d?w=600&q=80",
                    false, 4.9, 15,
                    "Flights to Leh, guesthouse stays, Pangong Lake camping, monastery visits, jeep safaris",
                    "Personal gear, optional motorcycle rental, tips"),

            new Trip("N007", "Andaman Island Retreat", "Port Blair", "India",
                    5, 600.0,
                    "Crystal-clear waters, pristine white beaches and vibrant coral reefs. Snorkel in Havelock Island, visit Radhanagar Beach and explore Cellular Jail.",
                    "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=600&q=80",
                    false, 4.7, 20,
                    "Flights, island transfers, beach resort, snorkeling, glass-bottom boat, Cellular Jail",
                    "Scuba diving, personal shopping, optional excursions"),

            new Trip("N008", "Coorg Coffee Trails", "Coorg", "India",
                    4, 350.0,
                    "Misty hills, coffee plantations, waterfalls and wildlife in Karnataka's Scotland. Perfect for a peaceful and refreshing nature retreat.",
                    "https://images.unsplash.com/photo-1545205597-3d9d02c29597?w=600&q=80",
                    false, 4.5, 20,
                    "Plantation resort, all meals, coffee estate tour, Abbey Falls, Nagarhole safari",
                    "Personal shopping, optional treks, tips"),

            new Trip("N009", "Varanasi Spiritual Journey", "Varanasi", "India",
                    4, 280.0,
                    "Experience the spiritual capital of India. Witness the sacred Ganga Aarti, boat rides at dawn, ancient ghats and the timeless culture of Banaras.",
                    "https://images.unsplash.com/photo-1561361058-c24cecae35ca?w=600&q=80",
                    false, 4.6, 25,
                    "Heritage hotel, Ganga Aarti, sunrise boat ride, guided temple tour, Sarnath excursion",
                    "Personal meals, tips, shopping"),

            new Trip("N010", "Rajasthan Desert Safari", "Jaisalmer", "India",
                    5, 420.0,
                    "Ride camels into the Thar Desert, spend a night under the stars at a desert camp and explore the golden sandstone Jaisalmer Fort.",
                    "https://images.unsplash.com/photo-1548013146-72479768bada?w=600&q=80",
                    false, 4.7, 22,
                    "Heritage hotel, desert camp, camel safari, cultural evening, folk music & dance, jeep ride",
                    "Personal shopping, optional ATV ride, tips"),

            // 5 NEW NATIONAL TRIPS
            new Trip("N011", "Darjeeling Tea & Hills", "Darjeeling", "India",
                    5, 380.0,
                    "Ride the iconic Toy Train through misty tea gardens, watch the sunrise over Kanchenjunga from Tiger Hill and explore the charming colonial hill station.",
                    "https://images.unsplash.com/photo-1571411169729-f05c59e4bafe?w=600&q=80",
                    false, 4.6, 20,
                    "Heritage hotel, Toy Train joy ride, Tiger Hill sunrise, tea estate tour, local sightseeing, all meals",
                    "Personal shopping, tips, optional ropeway"),

            new Trip("N012", "Meghalaya Living Roots", "Shillong", "India",
                    6, 450.0,
                    "Discover the wettest place on Earth, the stunning Double Decker Living Root Bridge, Nohkalikai Falls and the crystal-clear waters of Dawki River.",
                    "https://images.unsplash.com/photo-1624463729018-a4efdc9e9e05?w=600&q=80",
                    false, 4.8, 15,
                    "Flights to Guwahati, hotel in Shillong, Root Bridge trek, Cherrapunji excursion, Dawki river boat, all transfers",
                    "Personal meals at Cherrapunji, tips, optional caving"),

            new Trip("N013", "Rishikesh Yoga & Rafting", "Rishikesh", "India",
                    4, 320.0,
                    "The yoga capital of the world meets adrenaline adventure. White-water raft on the Ganges, attend evening Ganga Aarti at Triveni Ghat and meditate in the Himalayas.",
                    "https://images.unsplash.com/photo-1591802282935-f7b3a5bf0fc4?w=600&q=80",
                    false, 4.7, 25,
                    "Ashram/camp stay, river rafting, yoga & meditation classes, Ganga Aarti, bungee jumping, Beatles Ashram visit",
                    "Personal food, optional cliff jumping, tips"),

            new Trip("N014", "Hampi Heritage Trek", "Hampi", "India",
                    4, 290.0,
                    "Step into the ruins of the Vijayanagara Empire. Bouldering landscapes, ancient temples, coracle rides on the Tungabhadra and vibrant local bazaars.",
                    "https://images.unsplash.com/photo-1582972236019-ea4af5ffe587?w=600&q=80",
                    false, 4.5, 20,
                    "Guesthouse stay, guided heritage walk, Virupaksha Temple, Vittala Temple, coracle ride, cycle tour, all transfers",
                    "Personal meals, tips, optional bouldering guide"),

            new Trip("N015", "Sikkim Himalayan Escape", "Gangtok", "India",
                    7, 550.0,
                    "Explore pristine Himalayan landscapes, Buddhist monasteries, yak rides at Tsomgo Lake and flower-filled valleys in India's cleanest state.",
                    "https://images.unsplash.com/photo-1506905925346-21bda4d32df4?w=600&q=80",
                    false, 4.8, 18,
                    "Flights to Bagdogra, hotel in Gangtok, Tsomgo Lake, Rumtek Monastery, Pelling, Nathula Pass permit, all transfers",
                    "Personal gear, tips, optional helicopter tour"),
        };

        for (Trip trip : internationalTrips) {
            if (tripRepository.findByPackageId(trip.getPackageId()).isEmpty()) {
                tripRepository.save(trip);
            }
        }
        for (Trip trip : nationalTrips) {
            if (tripRepository.findByPackageId(trip.getPackageId()).isEmpty()) {
                tripRepository.save(trip);
            }
        }

        System.out.println("✅ Sample data loaded successfully! (15 international + 15 national trips)");
    }
}
