package com.company.earthquake.controller;

import com.company.earthquake.model.Earthquake;
import com.company.earthquake.model.Filter;
import com.company.earthquake.service.EarthquakeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;

/**
 * @author mahmutcandurak
 */

@Controller
public class EarthquakeController {

    private final EarthquakeService earthquakeService;

    public EarthquakeController(EarthquakeService earthquakeService) {
        this.earthquakeService = earthquakeService;
    }



    @GetMapping(value = {"/","/search"} )
    public String searchForm(Model model) {
        model.addAttribute("filter",  new Filter());
        return "homepage";
    }

    @PostMapping("/search")
    public String searchSubmit(@ModelAttribute Filter filter, Model model){

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = "";
        String endTime = "";
        if (filter.getStarttime() != null) {
            startTime = simpleDateFormat.format(filter.getStarttime());
        }

        if (filter.getEndtime() != null) {
            endTime = simpleDateFormat.format(filter.getEndtime());
        }

        Earthquake earthquake = earthquakeService.getEarthquake(startTime, endTime, filter.getMinmagnitude(), filter.getCountry());
        model.addAttribute("earthquake", earthquake);
        return "result";
    }



}
