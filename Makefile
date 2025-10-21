PROJECT_NAME=licensing-example-java
SERVICE_NAME=licensing-example-java
SERVICE_PLAN=licensing-example-java
DOCKER_PLATFORM?=linux/amd64
MAIN_RESOURCE_NAME=licensing-example-java
ENVIRONMENT=Dev
CLOUD_PROVIDER=azure
REGION=eastus2

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
	mvn --update-snapshots clean verify

.PHONY: run
run:
	echo "Starting service"
	java -cp target/licensing-example-java-jar-with-dependencies.jar com.omnistrate.HttpServerApp

.PHONY: verify
verify: 
	echo "Verifying service"
	helm lint ./charts/helm

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
	@echo "Replacing chart version from Chart.yaml"
	@CHART_VERSION=$$(grep '^version:' charts/helm/Chart.yaml | awk '{print $$2}'); \
	IMAGE_VERSION=$$(gh release list --limit 1 --json tagName --jq '.[0].tagName' | sed 's/^v//'); \
	echo "Chart version: $$CHART_VERSION"; \
	echo "Image version: $$IMAGE_VERSION"; \
	cp spec.yaml spec.yaml.tmp; \
	sed -i.bak "s#<CHART_VERSION>#$$CHART_VERSION#g" spec.yaml.tmp; \
	sed -i.bak "s#<IMAGE_VERSION>#$$IMAGE_VERSION#g" spec.yaml.tmp; \
	rm spec.yaml.tmp.bak; \
	cat spec.yaml.tmp; \
	echo "Releasing service plan to Omnistrate"; \
	omnistrate-ctl build -s ServicePlanSpec -f spec.yaml.tmp --product-name ${SERVICE_NAME} --environment ${ENVIRONMENT} --environment-type ${ENVIRONMENT} --release-as-preferred; \
	rm spec.yaml.tmp

.PHONY: create
create:
	@omnistrate-ctl instance create --environment ${ENVIRONMENT} --cloud-provider ${CLOUD_PROVIDER} --region ${REGION} --plan ${SERVICE_PLAN} --service ${SERVICE_NAME} --resource ${MAIN_RESOURCE_NAME} 

.PHONY: list
list:
	@omnistrate-ctl instance list --filter=service:${SERVICE_NAME},plan:${SERVICE_PLAN} --output json

.PHONY: delete-all
delete-all:
	@echo "Deleting all instances..."
	@for id in $$(omnistrate-ctl instance list --filter=service:${SERVICE_NAME},plan:${SERVICE_PLAN} --output json | jq -r '.[].instance_id'); do \
		echo "Deleting instance: $$id"; \
		omnistrate-ctl instance delete $$id; \
	done

.PHONY: destroy
destroy: delete-all-wait
	@echo "Destroying service: ${SERVICE_NAME}..."
	@omnistrate-ctl service delete ${SERVICE_NAME}

.PHONY: delete-all-wait
delete-all-wait:
	@echo "Deleting all instances and waiting for completion..."
	@instances_to_delete=$$(omnistrate-ctl instance list --filter=service:${SERVICE_NAME},plan:${SERVICE_PLAN} --output json | jq -r '.[].instance_id'); \
	if [ -n "$$instances_to_delete" ]; then \
		for id in $$instances_to_delete; do \
			echo "Deleting instance: $$id"; \
			omnistrate-ctl instance delete $$id; \
		done; \
		echo "Waiting for instances to be deleted..."; \
		while true; do \
			remaining=$$(omnistrate-ctl instance list --filter=service:${SERVICE_NAME},plan:${SERVICE_PLAN} --output json | jq -r '.[].instance_id'); \
			if [ -z "$$remaining" ]; then \
				echo "All instances deleted successfully"; \
				break; \
			fi; \
			echo "Still waiting for deletion to complete..."; \
			sleep 10; \
		done; \
	else \
		echo "No instances found to delete"; \
	fi