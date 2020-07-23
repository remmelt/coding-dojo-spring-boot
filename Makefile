IMAGE_NAME := weather-app:0.0.1
MVN_CMD := ./mvnw

.PHONY: build
build:
	${MVN_CMD} package

.PHONY: clean
clean:
	${MVN_CMD} clean

.PHONY: local
local: dev-up
	${MVN_CMD} spring-boot:run -Dspring.profiles.active=dev

.PHONY: dev-up
dev-up:
	docker-compose up -d

.PHONY: dev-down
dev-down:
	docker-compose down --remove-orphans

.PHONY: package
package: clean build
	docker build -t ${IMAGE_NAME} .

