package com.assignment.spring;

import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.HttpResponse;

import java.io.File;
import java.nio.file.Files;

import static org.mockserver.model.HttpRequest.request;

public class TestUtils {
    public static void setMockServerExpectations(ClientAndServer mockServer, HttpResponse response) {
        mockServer
                .when(request().withMethod("GET").withPath("/data/2.5/weather"))
                .respond(response);
    }

    public static String readFile(String fileName) throws Exception {
        return Files.readString(new File(TestUtils.class.getClassLoader().getResource(fileName).getFile()).toPath());
    }
}
