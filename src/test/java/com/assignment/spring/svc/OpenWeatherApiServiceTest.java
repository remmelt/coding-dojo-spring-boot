package com.assignment.spring.svc;

import com.assignment.spring.AppConfig;
import com.assignment.spring.TestUtils;
import com.assignment.spring.api.OpenWeatherApiService;
import com.assignment.spring.persistence.WeatherRepository;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.stop.Stop.stopQuietly;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpenWeatherApiServiceTest.TestConfigNoDb.class)
public class OpenWeatherApiServiceTest {
    private static ClientAndServer mockServer;

    private OpenWeatherApiService openWeatherApiService;

    @Autowired
    private RestTemplate restTemplate;

    @BeforeClass
    public static void startMockServer() {
        mockServer = startClientAndServer();
    }

    @AfterClass
    public static void stopMockServer() {
        stopQuietly(mockServer);
    }

    @Before
    public void init() {
        openWeatherApiService = new OpenWeatherApiService(restTemplate,
                String.format("http://127.0.0.1:%d/data/2.5/weather", mockServer.getPort()),
                "nothing"
        );
        mockServer.reset();
    }

    @Test
    public void givenExistingCityShouldReturnWeatherResponse() throws Exception {
        TestUtils.setMockServerExpectations(
                mockServer,
                response()
                        .withStatusCode(200)
                        .withBody(TestUtils.readFile("weather-api-responses/amsterdam.json"))
                        .withContentType(MediaType.JSON_UTF_8)
        );

        var actual = openWeatherApiService.byCity("amsterdam");

        mockServer.verify(request().withPath("/data/2.5/weather"));

        assertThat(actual).isNotEmpty();
        var actualResponse = actual.get();
        assertThat(actualResponse.getClouds().getAll()).isEqualTo(75);
    }

    @Test
    public void givenUnknownCityShouldReturnEmpty() {
        TestUtils.setMockServerExpectations(mockServer, response().withStatusCode(404));

        var actual = openWeatherApiService.byCity("amsterdam");
        mockServer.verify(request().withPath("/data/2.5/weather"));
        assertThat(actual).isEmpty();
    }

    @Test(expected = IllegalArgumentException.class)
    public void givenIncorrectUrlShouldThrow() {
        openWeatherApiService = new OpenWeatherApiService(restTemplate, "this-is-not-ok", "nothing");
        openWeatherApiService.byCity("amsterdam");
    }

    @Configuration
    static class TestConfigNoDb extends AppConfig {
        @Bean
        public WeatherRepository weatherRepository() {
            return Mockito.mock(WeatherRepository.class);
        }
    }
}
