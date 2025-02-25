{{- define "licensing-example-java.fullname" -}}
{{- printf "%s-%s" .Release.Name .Chart.Name | trunc 63 | trimSuffix "-" -}}
{{- end -}}

{{- define "licensing-example-java.selectorLabels" -}}
{{- include "licensing-example-java.labels" . -}}
{{- end -}}

{{- define "licensing-example-java.labels" -}}
helm.sh/chart: {{ include "licensing-example-java.chart" . }}
{{ include "licensing-example-java.selectorLabels" . }}
{{- end -}}

{{- define "licensing-example-java.chart" -}}
{{ .Chart.Name }}-{{ .Chart.Version }}
{{- end -}}
