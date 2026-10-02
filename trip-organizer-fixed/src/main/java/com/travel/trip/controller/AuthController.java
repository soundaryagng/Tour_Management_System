package com.travel.trip.controller;

import com.travel.trip.model.User;
import com.travel.trip.service.AuthService;
import com.travel.trip.service.BookingService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private BookingService bookingService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email, @RequestParam String password,
                        HttpSession session, Model model) {
        User user = authService.login(email, password);
        if (user != null) {
            session.setAttribute("user", user);
            if ("ADMIN".equals(user.getUserType())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/dashboard";
        }
        model.addAttribute("error", "Invalid email or password");
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email,
                           @RequestParam String phone, @RequestParam String password,
                           Model model) {
        User user = authService.register(email, password, name, phone);
        if (user != null) {
            return "redirect:/login?registered=true";
        }
        model.addAttribute("error", "Email already exists. Please use a different email.");
        return "register";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";
        var bookings = bookingService.getUserBookings(user);
        model.addAttribute("totalBookings", bookings.size());
        model.addAttribute("totalTrips", bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getBookingStatus())).count());
        model.addAttribute("loyaltyPoints", bookings.stream()
                .filter(b -> "CONFIRMED".equals(b.getBookingStatus()))
                .mapToLong(b -> (long)(b.getTotalAmount() / 10)).sum());
        model.addAttribute("recentBookings", bookings.stream().limit(5).toList());
        return "dashboard";
    }
}
