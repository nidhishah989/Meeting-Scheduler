/******** JUST FOR HOME PAGE - NIDHI SHAH**********/
package org.nidhishah.meetingscheduler.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String getStartPage(){
        return "home_page";
    }
}
