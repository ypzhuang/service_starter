package com.bdease.spm.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@Api(tags = {"Home"})
public class HomeController extends BaseController {
    @RequestMapping(value = "/ping", method = GET)
    String ping() {
        DateTime dt = new DateTime();
        DateTimeFormatter fmt = ISODateTimeFormat.dateTime();
        return String.format("Pang %s", dt.toString(fmt));
    }

    @GetMapping("/")
    public String home() {    	
        return "Sipimo service is running.";
    } 
    
    @GetMapping("/adminPage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String adminRole() {
        return "Only ROLE_ADMIN can see this page.";
    }

    @GetMapping("/allPage")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER','ROLE_GUEST')")
    public String allRole() {
    	 return "Only All Roles can see this page.";
    }
    
    @GetMapping("/userPage")
    @PreAuthorize("hasAnyRole('ROLE_USER')")
    public String userPage() {
    	 return "Only User Roles can see this page.";
    }
    
    @GetMapping("/guestPage")
    @PreAuthorize("hasAnyRole('ROLE_GUEST')")
    public String guestPage() {
    	 return "Only Guest Roles can see this page.";
    }
}
