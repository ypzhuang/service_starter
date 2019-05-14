package com.hp.tiger.starter.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;
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
        return "Starter is running.";
    }    
}
