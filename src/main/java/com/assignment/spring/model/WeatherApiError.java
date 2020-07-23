package com.assignment.spring.model;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST, reason = "Weather API error")
public class WeatherApiError extends RuntimeException {
}
