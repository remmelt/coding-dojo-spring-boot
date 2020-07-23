package com.assignment.spring.svc;

import com.assignment.spring.model.WeatherApiError;
import com.assignment.spring.persistence.model.WeatherEntity;
import org.springframework.stereotype.Service;

@Service
public interface WeatherService {
    WeatherEntity byCity(String city) throws WeatherApiError;
}
