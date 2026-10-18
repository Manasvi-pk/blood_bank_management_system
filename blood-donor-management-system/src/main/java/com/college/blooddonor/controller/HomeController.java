
package com.college.blooddonor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping({"/", "/dashboard"})
    public String home() {
        return "home";
    }

    @GetMapping("/recipients")
    public String recipients() {
        return "recipients";
    }

    @GetMapping("/donations")
    public String donations() {
        return "donations";
    }

    @GetMapping("/inventory")
    public String inventory() {
        return "inventory";
    }

    @GetMapping("/requests")
    public String requests() {
        return "requests";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
