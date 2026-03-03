# Rest Assured Enterprise Framework

Enterprise-grade API automation framework built with resilience, observability, and production-level architecture principles.

This project is not just an API test suite.
It is a resilient API execution platform designed to simulate real-world distributed system behavior.

---

# 1. Purpose

Most API automation frameworks focus only on:

* Status code validation
* Schema validation
* Basic assertions

This framework focuses on:

* Service isolation
* Retry logic (idempotency-aware)
* Circuit breaker protection
* Chaos simulation
* Observability and telemetry
* Per-service metrics
* SLA validation
* Secure CI/CD matrix strategy
* True parallel execution safety

The goal is to engineer automation like a production microservice client.

---

# 2. Supported Services

The framework supports multiple services within one unified architecture:

| Service  | Type     | Notes                                      |
| -------- | -------- | ------------------------------------------ |
| ReqRes   | Mocked   | Uses WireMock for deterministic simulation |
| Petstore | Real API | Public Swagger API                         |
| GitHub   | Real API | Token-based authentication required        |

Each service is fully isolated in configuration and resilience behavior.

---

# 3. Architecture Overview

High-level execution flow:

```
Test Layer (TestNG)
        ↓
BaseApiClient (Unified Execution Engine)
        ↓
Retry Executor
        ↓
Circuit Breaker (Per Service)
        ↓
Chaos Injector (Optional)
        ↓
HTTP Execution
        ↓
Observability + Metrics Capture
        ↓
Response Validation
```

Key architectural characteristics:

* No shared global mutable state
* Service-specific configuration resolution
* Dynamic mock override (ReqRes only)
* Thread-safe correlation tracking
* Metrics recorded per service
* Config-driven feature toggles

---

# 4. Project Structure

```
framework/
 ├── client/              # Service clients (ReqresClient, PetstoreClient, GitHubClient)
 ├── core/
 │    ├── http/           # BaseApiClient, HttpMethod
 │    ├── retry/          # RetryExecutor, RetryPolicy
 │    ├── breaker/        # CircuitBreaker implementation
 │    ├── chaos/          # Chaos injection layer
 │    ├── mock/           # WireMockManager
 │    ├── metrics/        # MetricsCollector
 │    ├── observability/  # CorrelationManager
 │    ├── validation/     # ResponseValidator
 │    └── config/         # ServiceConfigResolver
 ├── data/                # Thread-safe test data generation
tests/
 ├── base/                # BaseTest
 ├── reqres/              # Mock-based tests
 ├── petstore/            # Real API tests
 ├── github/              # Authenticated tests
.github/
 └── workflows/           # CI pipeline
```

---

# 5. Core Engineering Components

## 5.1 Retry Engine

Features:

* Retries only idempotent methods (GET)
* Retries only on retryable status codes (5xx, 408)
* Exponential backoff
* Fully testable using WireMock scenario simulation

Purpose:
Simulate real network instability without masking application failures.

---

## 5.2 Circuit Breaker (Per Service)

Each external service has its own breaker.

States:

* CLOSED
* OPEN
* HALF-OPEN

Prevents cascading failures when an external dependency becomes unstable.

Configurable:

* Failure threshold
* Recovery timeout

---

## 5.3 Chaos Injection Layer

Controlled instability simulation.

Enable via runtime flags:

```
-Dchaos.enabled=true
-Dchaos.failure.rate=0.3
-Dchaos.latency.ms=500
```

Capabilities:

* Inject artificial latency
* Inject random failures
* Stress resilience layer without modifying test code

---

## 5.4 Observability Layer

Every request:

* Generates Correlation ID
* Logs request + response metadata
* Records latency
* Tracks service-level metrics

At suite end:

```
Service: GITHUB | Calls: 20 | Avg: 1502ms | Max: 3605ms
Service: PETSTORE | Calls: 10 | Avg: 1780ms | Max: 2467ms
Service: REQRES | Calls: 3 | Avg: 287ms | Max: 385ms
```

This transforms automation into performance visibility tooling.

---

## 5.5 Metrics Collector

Captures:

* Total calls per service
* Average latency
* Maximum latency
* Per-service histogram basis

Can be extended to push metrics to Prometheus or Grafana.

---

## 5.6 SLA Validation

ResponseValidator enforces:

* 2xx validation
* JSON validation
* Schema validation
* Response time limits

SLA is configurable.

---

## 5.7 Parallel Execution Safety

* TestNG parallel execution
* ThreadLocal Correlation IDs
* ThreadLocal data context
* Deterministic seeded data generation

No race conditions across services.

---

# 6. Running the Framework

## Basic Execution

```
mvn clean test -Denv=qa
```

---

## Run with Chaos Enabled

PowerShell:

```
mvn clean test -Denv=qa "-Dchaos.enabled=true" "-Dchaos.failure.rate=0.3"
```

Linux / Mac:

```
mvn clean test -Denv=qa -Dchaos.enabled=true -Dchaos.failure.rate=0.3
```

---

## Run Smoke Only

```
mvn clean test -Dgroups=smoke
```

---

# 7. GitHub Authentication Setup

Required for GitHub API tests.

## Step 1 — Generate Token

GitHub → Settings → Developer Settings → Personal Access Token

Required scopes:

* repo

---

## Step 2 — Add Repository Secrets

Repository → Settings → Secrets → Actions

Add:

Name:

```
GH_API_TOKEN
```

Value:
Your personal access token

Add:

Name:

```
GH_USERNAME
```

Value:
Your GitHub username

---

# 8. CI/CD Pipeline

GitHub Actions Matrix Strategy:

Runs:

* Smoke tests
* Full regression
* Chaos-enabled resilience suite

Automatically:

* Builds project
* Executes tests
* Generates Allure report
* Uploads report artifact

---

# 9. Allure Reporting

Generate locally:

```
mvn allure:serve
```

CI uploads:

```
target/site/allure-maven-plugin
```

---

# 10. Design Principles

This framework follows:

* Isolation over convenience
* Config-driven architecture
* Fail-fast configuration validation
* No service cross-contamination
* Deterministic parallel execution
* Observability-first design
* Resilience testing as a requirement

---

# 11. How to Add a New Service

Step 1:
Add new ServiceType enum

Step 2:
Add configuration keys

Step 3:
Create new Client extending BaseApiClient

Step 4:
(Optional) Register CircuitBreaker for new service

Step 5:
Add tests under tests/<service>

No core modification required.

---

# 12. What Makes This Enterprise-Grade

* Idempotency-aware retry
* Per-service circuit breaker
* Chaos injection toggle
* Dynamic mock override
* Correlation ID tracking
* SLA enforcement
* Metrics collection
* CI matrix execution
* Secure secret handling
* Parallel-safe design

---

# 13. Future Enhancements

* Prometheus metrics export
* Grafana dashboards
* Memory soak testing
* Heap leak validation
* Distributed tracing integration
* Resilience configuration via YAML
* Load-based stress execution mode

---

# 14. Conclusion

This project demonstrates:

Automation engineered with production-level thinking.

It validates not only correctness —
but resilience, performance, and failure behavior.

This is the difference between writing tests
and engineering test systems.

---

## Author

Manoj Kumar
SDET | Automation Engineer
Java | Selenium | TestNG | CI/CD

---
