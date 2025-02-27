# Helm Chart for Usage Example of Omnistrate Licensing SDK for Java

## Introduction

This chart bootstraps a [licensing-example-java](https://github.com/omnistrate-community/licensing-example-java) deployment on a Kubernetes cluster using the Helm package manager.

## Prerequisites

- Kubernetes 1.12+
- Helm 3.1.0+

## Installing the Chart

To install the chart with the release name `my-release`:

```bash
$ helm repo add licensing-example-java https://omnistrate-community.github.io/licensing-example-java
$ kubectl create namespace licensing-example-java 
$ helm install licensing-example-java licensing-example-java/licensing-example-java --namespace licensing-example-java
```

These commands add the Helm repository and install the chart with the default configuration.

## Uninstalling the Chart

To uninstall/delete the `my-release` deployment:

```bash
$ helm uninstall licensing-example-java --namespace licensing-example-java
```

The command removes all the Kubernetes components associated with the chart and deletes the release.

## Configuration

The following table lists the configurable parameters of the `licensing-example-java` chart and their default values.

| Parameter                | Description                                     | Default                                                 |
|--------------------------|-------------------------------------------------|---------------------------------------------------------|
| `replicaCount`           | Number of replicas                              | `1`                                                     |
| `image.repository`       | Image repository                                | `ghcr.io/omnistrate-community/licensing-example-java`   |
| `image.pullPolicy`       | Image pull policy                               | `IfNotPresent`                                          |
| `image.tag`              | Image tag                                       | `latest`                                                |
| `ingress.enabled`        | Enable ingress controller resource              | `true`                                                  |
| `ingress.className`      | Ingress class name                              | `nginx`                                                 |
| `ingress.annotations`    | Ingress annotations                             | `{}`                                                    |
| `ingress.host`           | Hostname for the ingress                        | `frontendlb.instance-fpi05qn33.hc-pelsk80ph.us-east-2.aws.f2e0a955bb84.cloud` |
| `subscriptionSecret.enabled` | Enable subscription secret                | `true`                                                  |

Specify each parameter using the `--set key=value[,key=value]` argument to `helm install`. For example:

```bash
$ helm install my-release my-repo/licensing-example-java --namespace my-namespace --set replicaCount=2
```

Alternatively, a YAML file that specifies the values for the parameters can be provided while installing the chart. For example:

```bash
$ helm install my-release my-repo/licensing-example-java --namespace my-namespace -f values.yaml
```

## License

This chart is licensed under the Apache 2.0 License.
