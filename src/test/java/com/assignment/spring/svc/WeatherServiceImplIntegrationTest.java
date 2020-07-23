package com.assignment.spring.svc;

import com.assignment.spring.PostgresMockedTest;
import com.assignment.spring.TestUtils;
import com.assignment.spring.persistence.WeatherRepository;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.stop.Stop.stopQuietly;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "weather.app.api.url=http://127.0.0.1:9999/data/2.5/weather",
        "weather.app.api.key=x"
})
public class WeatherServiceImplIntegrationTest extends PostgresMockedTest {
    private static ClientAndServer mockServer;

    @Autowired
    private WeatherController weatherController;

    @Autowired
    private WeatherRepository weatherRepository;

    @BeforeClass
    public static void startMockServer() {
        mockServer = startClientAndServer(9999);
    }

    @AfterClass
    public static void stopMockServer() {
        stopQuietly(mockServer);
    }

    @Before
    public void init() {
        mockServer.reset();
    }

    @Test
    public void givenHappyCallShouldReturnCorrectEntityAndCallSave() throws Exception {
        TestUtils.setMockServerExpectations(
                mockServer,
                response()
                        .withStatusCode(200)
                        .withBody(TestUtils.readFile("weather-api-responses/amsterdam.json"))
                        .withContentType(MediaType.JSON_UTF_8)
        );

        var actual = weatherController.weather("amsterdam");

        assertThat(actual.getCountry()).isEqualTo("NL");

        var optionalFoundEntity = weatherRepository.findById(actual.getId());
        assertThat(optionalFoundEntity).isNotEmpty();

        var foundEntity = optionalFoundEntity.get();
        assertThat(foundEntity.getCity()).isEqualTo("Amsterdam");
        assertThat(foundEntity.getCountry()).isEqualTo("NL");
        assertThat(foundEntity.getTemperature()).isEqualTo(292.5);
    }
}
