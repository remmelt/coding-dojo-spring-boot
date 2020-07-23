package com.assignment.spring.svc;

import com.assignment.spring.api.model.Main;
import com.assignment.spring.api.model.Sys;
import com.assignment.spring.api.model.WeatherResponse;
import com.assignment.spring.persistence.WeatherRepository;
import com.assignment.spring.persistence.model.WeatherEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class WeatherServiceImplUnitTest {
    @Mock
    private WeatherRepository weatherRepository;

    @Mock
    private WeatherApiService weatherApiService;

    @Captor
    private ArgumentCaptor<WeatherEntity> weatherEntityArgumentCaptor;

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Test
    public void givenHappyCallShouldReturnCorrectEntityAndCallSave() {
        var weatherResponse = happyWeatherResponse();

        when(weatherApiService.byCity(any())).thenReturn(Optional.of(weatherResponse));
        var actual = weatherService.byCity("amsterdam");

        verify(weatherRepository, times(1)).save(weatherEntityArgumentCaptor.capture());

        assertThat(weatherEntityArgumentCaptor.getAllValues()).hasSize(1);
        assertThat(weatherEntityArgumentCaptor.getAllValues().get(0).getTemperature()).isEqualTo(1.0);

        assertThat(actual.getCity()).isEqualTo("utrecht");
    }

    @Test
    public void givenAllValuesPresentMapperShouldReturnEntity() {
        var weatherResponse = happyWeatherResponse();
        var actual = weatherService.mapper(weatherResponse);
        assertThat(actual.getCity()).isEqualTo("utrecht");
        assertThat(actual.getCountry()).isEqualTo("nederland");
        assertThat(actual.getTemperature()).isEqualTo(1.0);
    }

    private WeatherResponse happyWeatherResponse() {
        Sys sys = new Sys();
        sys.setCountry("nederland");
        Main main = new Main();
        main.setTemp(1.0);
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setName("utrecht");
        weatherResponse.setSys(sys);
        weatherResponse.setMain(main);
        return weatherResponse;
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenSomeValuesNullMapperShouldThrow() {
        WeatherResponse weatherResponse = new WeatherResponse();
        weatherResponse.setName("utrecht");
        var actual = weatherService.mapper(weatherResponse);
        assertThat(actual.getCity()).isEqualTo("utrecht");
        assertThat(actual.getCountry()).isEqualTo("nederland");
        assertThat(actual.getTemperature()).isEqualTo(1.0);
    }
}
