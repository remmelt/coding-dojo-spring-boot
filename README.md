Spring Boot Coding Dojo
---

Welcome to my Spring Boot Coding Dojo solution! This was a lot of fun to build. Time permitting I would make it even nicer, see notes and todos below. Looking forward to your feedback!

# Building and running 

## Testing
Tests use docker for starting up postgres in a known state. Make sure to have docker installed on your local machine.

## Local dev run
- Start Postgres using `make dev-up`, this uses docker compose. Note the `make dev-down`.
- `export WEATHER_APP_API_KEY=<your api key>`
- Run `make local` to run the application locally.

## Production
- Create a docker container using `make package`, this will clean, build, run tests, and create a docker image
- TODO: hook in to deployment stack of choice, set environment variables:
    - WEATHER_APP_API_KEY
    - DATASOURCE_URL
    - DATASOURCE_PW
- Run the container, e.g. `docker run -it -p 8080:8080 -e WEATHER_APP_API_KEY=... -e DATASOURCE_URL=jdbc:postgresql://<your IP>:5432/postgres -e DATASOURCE_PW=example weather-app:0.0.1`

# Notes

Re-used the entity as the object that is being returned to the user when a request comes in. In a larger, more complex application it could make sense to decouple these models.

Set `export WEATHER_APP_API_KEY=<... your api key ...>` in your environment to a valid openweathermap API key.

In larger applications I would use something like Immutables or Lombok to create data classes like the WeatherEntity, benefits include less boilerplate code, certainty that all fields will be set, and certainty that values will map to the correct fields.

For running this service on a local computer we will need a running version of Postgres. For this, use the included docker-compose file by running the command `make dev-up` and `make dev-down`.

# TODOs 
- more extensive testing (more error testing, like db unavailable, api times out, etc)
- more precise feedback to the user when some error happens (now just one type of message)
- api definition (e.g. OpenAPI spec)
- helm chart/nomad file/docker stack spec
- integration into CI/CD pipeline
- health check endpoint
- add time stamp to weatherEntity
- export metrics (hits to api, error rate and latency on openweatherapi, etc)
- export logs as json to e.g. ELK
