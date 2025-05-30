# Usage Example of Omnistrate Licensing SDK for Java

![Build](https://github.com/omnistrate-community/licensing-example-java/actions/workflows/build.yml/badge.svg)
![Package](https://github.com/omnistrate-community/licensing-example-java/actions/workflows/package.yml/badge.svg)
![Chart release](https://github.com/omnistrate-community/licensing-example-java/actions/workflows/release-charts.yml/badge.svg)

## Overview

This example project demonstrates how to use the Omnistrate Licensing SDK in a Java application, with a focus on validating license files generated by Omnistrate. It includes examples of how to integrate the SDK into your Java projects and use its validation functions to ensure licenses are valid.

## Structure

### Java project

The [HttpServerApp.java](./src/main/java/com/omnistrate/HttpServerApp.java) file is the entry point of the Java application. It demonstrates how to use the Omnistrate Licensing SDK to validate your mounted licenses.

This is the validation method that can be used to validate a license key for a product in Omnistrate. With a simple call to this method, you can:

- confirm the validity of the certificate that signed the license
- validate the license signature
- validate the license expiration date
- validate the unique SKU configured in Omnistrate maps with the product
- validate that the injected variable containing the instance-id maps with the license

> **Note:** The SDK logic assumes that the license file is mounted in the following folder: `/var/subscription/`. When using Omnistrate there is no need to add any additional configuration to manage certificate rotation, suspension or renewal of the license is automated in a simple way. You can explore the implementation of the [Open Source SDK](https://github.com/omnistrate-oss/omnistrate-licensing-sdk-go).

### Docker file

The `Dockerfile` is used to create a Docker image for the Java application. It includes instructions for setting up the Go environment, copying the application code, installing dependencies, and building the application. This allows the application to be containerized and run in any environment that supports Docker.

### Helm chart

The `/charts` folder contains the Helm charts for deploying the application on a Kubernetes cluster. The Helm chart defines the resources and configurations needed to run the application, including deployments, services, and ingress.

### CI integration

The `.github/workflows` directory contains GitHub Actions workflows for continuous integration. These workflows automate the build and package processes for the Java application. The `build.yml` workflow handles the compilation and testing of the application, the `package.yml` workflow manages the packaging and publishing of the application artifacts in Github Packages, and the `release-charts.yml` workflow manages the release of Helm charts.

### Service specification

The `spec.yaml` file defines the Kubernetes Service resource for the application. It specifies the service and helm configuration to deploy it.

### Make file automation

The `Makefile` includes various automation scripts to streamline the development and deployment processes. It contains commands for building the application, releasing the application to Omnistrate and creating instances for testing, all done using `omnistrate-ctl`.

### Alternative setup - Compose specification

The `compose.yaml` file defines the service to run the Java application using Docker Compose. This file is ready for deployment in Omnistrate, ensuring that the application can be easily provisioned and managed in a cloud environment. Contains the configuration required to enable the Licensing feature in Omnistrate.

## Contributing

Want to contribute? Awesome! You can find information about contributing to this project in the [CONTRIBUTING](/CONTRIBUTING.md) page.

## License

This project is licensed under the MIT License. See the LICENSE file for details.

## About Omnistrate

[Omnistrate](https://omnistrate.com/) is the operating system for your SaaS, offering enterprise-grade capabilities: automated provisioning, serverless capabilities, auto-scaling, billing, monitoring, centralized logging, self-healing, intelligent patching and much more!
