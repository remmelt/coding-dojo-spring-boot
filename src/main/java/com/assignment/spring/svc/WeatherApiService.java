package com.assignment.spring.svc;

import com.assignment.spring.api.model.WeatherResponse;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

/**
 * The interface to some weather API
 */
@Service
public interface WeatherApiService {
    Optional<WeatherResponse> byCity(@NotNull String city);
}
