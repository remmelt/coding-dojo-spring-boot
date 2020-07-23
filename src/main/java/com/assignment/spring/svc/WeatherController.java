package com.assignment.spring.svc;

import com.assignment.spring.persistence.model.WeatherEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {
    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @RequestMapping("/weather")
    @ResponseBody
    public WeatherEntity weather(@RequestParam(name = "city") String city) {
        return weatherService.byCity(city);
    }
}
