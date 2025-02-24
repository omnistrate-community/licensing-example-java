PROJECT_NAME=licensing-example-java
DOCKER_PLATFORM?=linux/amd64
CGO_ENABLED=0

# Load variables from .env if it exists
ifneq (,$(wildcard .env))
    include .env
    export $(shell sed 's/=.*//' .env)
endif

# Java
.PHONY: all
all: build

.PHONY: build
build:
	echo "Verifying and building service"
	mvn --update-snapshots verify

.PHONY: run
run:
	echo "Starting service"
	java -cp target/licensing-example-java.jar com.omnistrate.HttpServerApp

# Docker 
.PHONY: docker-build 
docker-build:
	docker build --platform=${DOCKER_PLATFORM} -f ./build/Dockerfile -t ${PROJECT_NAME}:latest .
.PHONY: docker-build-arm64
docker-build-arm64:
	docker build --platform=linux/arm64 -f ./build/Dockerfile -t ${PROJECT_NAME}:latest .

.PHONY: docker-run
docker-run:
	echo "Starting service"
	docker run --platform=${DOCKER_PLATFORM} -p 8080:8080 ${PROJECT_NAME}:latest
.PHONY: docker-run-arm64
docker-run-arm64:
	echo "Starting service"
	docker run --platform=linux/arm64 -p 8080:8080 ${PROJECT_NAME}:latest

# Omnistrate
.PHONY: install-ctl
install-ctl:
	@brew install omnistrate/tap/omnistrate-ctl

.PHONY: upgrade-ctl
upgrade-ctl:
	@brew upgrade omnistrate/tap/omnistrate-ctl
	
.PHONY: login
login:
	cat ./.omnistrate.password | omnistrate-ctl login --email $(OMNISTRATE_EMAIL) --password-stdin

.PHONY: release
release:
	omnistrate-ctl build -f compose.yaml --name licensing-example-java --release-as-preferred

.PHONY: create
create:
	omnistrate-ctl instance create --environment Dev --cloud-provider aws --region us-east-2 --plan licensing-example-java --service licensing-example --resource web