package com.assignment.spring.api;

import com.assignment.spring.api.model.WeatherResponse;
import com.assignment.spring.svc.WeatherApiService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

@Service
public class OpenWeatherApiService implements WeatherApiService {
    private final RestTemplate restTemplate;

    private final String weatherApiUrl;

    private final String weatherApiKey;

    public OpenWeatherApiService(RestTemplate restTemplate,
                                 @Value("${weather.app.api.url}") String weatherApiUrl,
                                 @Value("${weather.app.api.key}") String weatherApiKey) {
        this.restTemplate = restTemplate;
        this.weatherApiUrl = weatherApiUrl;
        this.weatherApiKey = weatherApiKey;

        System.out.println(weatherApiKey);
    }

    @Override
    public Optional<WeatherResponse> byCity(@NotNull String city) {
        ResponseEntity<WeatherResponse> response;
        try {
            response = restTemplate.getForEntity(weatherApiUrl, WeatherResponse.class, parameters(city, weatherApiKey));
        } catch (HttpClientErrorException e) {
            return Optional.empty();
        }

        if (response.getStatusCode().isError() || response.getBody() == null) {
            return Optional.empty();
        }

        return Optional.of(response.getBody());
    }

    private Map<String, String> parameters(String city, String appId) {
        return Map.of("city", city, "appid", appId);
    }
}
