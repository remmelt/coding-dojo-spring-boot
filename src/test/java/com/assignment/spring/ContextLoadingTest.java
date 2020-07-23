package com.assignment.spring;

import com.assignment.spring.svc.WeatherController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {
        "weather.app.api.url=http://127.0.0.1:8080/weather",
        "weather.app.api.key=x"
})
public class ContextLoadingTest extends PostgresMockedTest {
    @Autowired
    private WeatherController controller;

    @Test
    public void contextLoads() {
        assertThat(controller).isNotNull();
    }
}
