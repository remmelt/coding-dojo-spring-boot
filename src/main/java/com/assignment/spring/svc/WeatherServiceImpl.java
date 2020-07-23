package com.assignment.spring.svc;

import com.assignment.spring.api.model.WeatherResponse;
import com.assignment.spring.model.WeatherApiError;
import com.assignment.spring.persistence.WeatherRepository;
import com.assignment.spring.persistence.model.WeatherEntity;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Service
public class WeatherServiceImpl implements WeatherService {
    private final WeatherRepository weatherRepository;
    private final WeatherApiService weatherApiService;

    public WeatherServiceImpl(WeatherRepository weatherRepository, WeatherApiService weatherApiService) {
        this.weatherRepository = weatherRepository;
        this.weatherApiService = weatherApiService;
    }

    @Override
    public WeatherEntity byCity(@NotNull String city) {
        // first, get the weather from the API
        WeatherResponse weatherResponse = weatherApiService.byCity(city).orElseThrow(WeatherApiError::new);

        // next, store in postgres
        WeatherEntity weatherEntity = mapper(weatherResponse);
        weatherRepository.save(weatherEntity);

        // finally, return the entity
        return weatherEntity;
    }

    WeatherEntity mapper(WeatherResponse response) {
        try {
            WeatherEntity entity = new WeatherEntity();
            entity.setCity(response.getName());
            entity.setCountry(response.getSys().getCountry());
            entity.setTemperature(response.getMain().getTemp());
            return entity;
        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
