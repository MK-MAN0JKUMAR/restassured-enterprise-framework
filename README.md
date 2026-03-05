# restassured-enterprise-framework – Enterprise API Automation Platform

Enterprise-grade **API automation and resilience testing framework** built with:

* Java
* Rest Assured
* TestNG
* Maven
* WireMock
* Allure Reporting
* CI/CD (GitHub Actions & Jenkins)

The framework is engineered not only to validate API correctness but also to simulate **real distributed system behavior**, including:

* retry handling
* circuit breaker protection
* chaos testing
* service isolation
* observability
* SLA validation

This project demonstrates **production-level API client architecture** rather than a traditional test suite.

---

# Project Goals

Most automation frameworks only verify:

* status codes
* response body validation
* schema checks

This framework goes further by testing **system reliability characteristics**.

Key engineering goals:

• Service isolation
• Idempotent retry strategy
• Circuit breaker resilience
• Chaos engineering simulation
• Observability and metrics
• SLA validation
• Deterministic parallel execution
• CI/CD pipeline orchestration

The objective is to simulate **how real microservices interact with external APIs**.

---

# System Architecture

This diagram shows the **complete execution flow of the framework**.

```mermaid
flowchart TD

A[TestNG Test Layer] --> B[Service Test Classes]

B --> C[Service Clients]

C --> D[BaseApiClient Execution Engine]

D --> E[Retry Executor]

E --> F[Circuit Breaker]

F --> G[Chaos Injection Layer]

G --> H[HTTP Request Execution]

H --> I[External Service / WireMock]

I --> J[Response Processing]

J --> K[Response Validator]

K --> L[Observability Layer]

L --> M[Metrics Collector]

M --> N[Test Result]
```

Execution pipeline:

```
Tests
 → Service Clients
 → Resilience Layer
 → Chaos Injection
 → HTTP Execution
 → Observability
 → Validation
```

This architecture mirrors **enterprise API client design**.

---

# Service Isolation Architecture

The framework supports **multiple APIs within a single automation platform**.

```mermaid
flowchart LR

A[Test Framework]

A --> B[Reqres Service]
A --> C[Petstore Service]
A --> D[GitHub Service]

B --> E[WireMock Simulation]

C --> F[Real API]

D --> G[GitHub API]

E --> H[Metrics]
F --> H
G --> H
```

### Supported Services

| Service  | Type     | Description                               |
| -------- | -------- | ----------------------------------------- |
| ReqRes   | Mocked   | Deterministic API simulation via WireMock |
| Petstore | Real API | Public Swagger API                        |
| GitHub   | Real API | Requires authentication token             |

Each service operates with **independent configuration and resilience policies**.

---

# Resilience Layer Architecture

The framework integrates **distributed system resilience patterns**.

```mermaid
flowchart TD

A[API Request] --> B[Retry Engine]

B --> C{Failure?}

C -- No --> D[Success Response]

C -- Yes --> E[Circuit Breaker]

E --> F{Breaker State}

F -- CLOSED --> G[Retry Request]

F -- OPEN --> H[Fail Fast]

F -- HALF OPEN --> I[Test Recovery Call]

G --> J[Chaos Injection]

J --> K[HTTP Execution]

K --> L[Observability + Metrics]

L --> M[Response Validator]
```

Implemented resilience mechanisms:

• Retry with exponential backoff
• Circuit breaker protection
• Chaos injection simulation
• Observability and metrics
• SLA validation

These patterns are widely used in **distributed systems and microservices architectures**.

---

# Distributed Testing Platform Architecture

This diagram illustrates how the framework can scale into a **testing platform**.

```mermaid
flowchart LR

A[Test Suites]

A --> B[Execution Engine]

B --> C[Service Clients]

C --> D[Resilience Layer]

D --> E[API Services]

B --> F[Metrics Collector]

F --> G[Metrics Backend]

G --> H[Observability Dashboard]

B --> I[Chaos Controller]

I --> D
```

This architecture supports future expansion into:

• observability dashboards
• distributed tracing
• metrics backend
• chaos testing platform

---

# Project Structure

```
restassured-enterprise-framework/
├── .github
│       └── workflows
│              └── api-tests.yml
│
├── src/
│   ├── main/
│   │   └── java/
│   │   	└── framework/
│   │       		├── client/
│   │       		│       ├── GitHubClient.java
│   │       		│       ├── PetstoreClient.java
│   │       		│	└── ReqresClient.java
│   │       		│
│   │       		├── constants/
│   │       		│       ├── PetstoreEndpoints.java
│   │       		│	├── ReqresEndpoints.java
│   │       		│	└── ServiceType.enum
│   │       		│
│   │       		├── core/
│   │       		│	├── annotation
│   │       		│	│	└── Service
│   │       		│	├── chaos
│   │       		│	│	├── ChaosConfig.java
│   │       		│	│	└── ChaosInjector.java
│   │       		│	├── config
│   │       		│	│	├── FrameworkConfig.java
│   │       		│	│	├── ServiceConfig.java
│   │       		│	│	└── ServiceConfigResolver.java
│   │       		│	├── exception
│   │       		│	│	├── ConfigException.enum
│   │       		│	│	└── FrameworkException.enum
│   │       		│	├── http
│   │       		│	│	├── BaseApiClient.java
│   │       		│	│	├── HttpMethod.enum
│   │       		│	│	├── RequestSpecFactory.java
│   │       		│	│	├── ResponseSpecFactory.java
│   │       		│	│	└── SensitiveHeaderFilter.java
│   │       		│	├── metrics
│   │       		│	│	└── MetricsCollector.java
│   │       		│	├── mock
│   │       		│	│	└── WireMockManager.java
│   │       		│	├── observability
│   │       		│	│	└── CorrelationManager.java
│   │       		│	├── pagination
│   │       		│	│	├──LinkHeaderParser.java
│   │       		│	│	└── PaginationHelper.java
│   │       		│	├── reporting
│   │       		│	│	└── AllureRestAssuredFilter.java
│   │       		│	├── resilience
│   │       		│	│	├──CircuitBreaker.java
│   │       		│	│	├──CircuitBreakerRegistry.java
│   │       		│	│	└── CircuitState.enum
│   │       		│	├── retry
│   │       		│	│	├──RetryContext.java
│   │      		│	│	├──RetryExecutor.java
│   │       		│	│	└── RetryPolicy.java
│   │       		│	├── schema
│   │       		│	│	├── JsonSchemaValidator.java
│   │       		│	│	└── SchemaLoader.java
│   │       		│	└── validation
│   │       		│		├──RateLimitValidator.java
│   │       		│		└── ResponseValidator.java
│   │       		│
│   │       		├── data/
│   │       		│	├── github/
│   │                   │       │        ├──builders
│   │       		│	│        │         └── RepoBuilder.java
│   │       		│	│        └── GitHubDataFactory.java
│   │       		│	├── petstore/
│   │                   │       │        ├──builders
│   │       		│	│        │         └── PetBuilder.java
│   │       		│	│        └── PetstoreDataFactory.java
│   │       		│	├── reqres/
│   │                   │       │        ├──builders
│   │       		│	│        │         └── ReqresUserBuilder.java
│   │       		│	│        └── ReqresDataFactory.java
│   │       		│	├──DataContext.java
│   │       		│       └──DataSeedManager.java
│   │       		│
│   │       		├── domain/
│   │       		│	├── common/
│   │       		│	│	└── ErrorResponse.java
│   │       		│	├── github/
│   │       		│	│	├── CreateRepoRequest.java
│   │       		│	│	├── PaginationResponse.java
│   │       		│	│	└── RepoResponse.java
│   │       		│	├── petstore/
│   │       		│	│	├── Category.java
│   │       		│	│	├── PetRequest.java
│   │       		│	│	└── PetResponse.java
│   │       		│	└── reqres/
│   │       		│		├── CreateUserRequest.java
│   │       		│		├── CreateUserResponse.java
│   │       		│		└── GetUserResponse.java
│   │       		└── utils (empty)
│   │
│   └── test/
│       ├── java/
│       │	├── framework
│       │       │	├── core
│       │       │	│     ├── listener
│       │       │	│     │        └── ServiceExecutionListener.java
│       │       │	│     └── service
│       │       │	│              └── ServiceRegistry.java
│       │       │	└── tools
│       │       │	      └── ServiceDiscoveryRunner.java
│       │       │
│       │	└── tests/
│       │    		├── base
│       │    		│	└── BaseTest.java
│       │    		├── github
│       │    		│	├── GitHubAuthNegativeTest.java
│    	│		│	├── GitHubConcurrencyTest.java
│    	│		│	├── GitHubDuplicateRepoNegativeTest.java
│    	│		│	├── GitHubPaginationTest.java
│    	│		│	├── GitHubRateLimitTest.java
│    	│		│	└── GitHubRepoSmokeTest.java
│       │    		├── petstore
│       │    		│	├── CreateGetDeletePetFlowTest.java
│    	│		│	├── CreatePetNegativeTest.java
│    	│		│	├── CreatePetTest.java
│    	│		│	├── FindPetByStatusTest.java
│    	│		│	└── UploadPetImageTest.java
│       │    		└── reqres
│       │    			├── stubs/
│       │    			│	├── CreateUserStub.java
│       │    			│	├── ErrorStubs.java
│       │    			│	├── GetUserStub.java
│       │    			│	└── RetrySimulationStub.java
│    	│			├── CreateUserNegativeTest.java
│    	│			├── CreateUserTest.java
│    	│			├── GetUsersTest.java
│    	│			├── ReqresBaseTest.java
│    	│			└── RetryValidationTest.java
│    	│
│   	└── resources/
│      		├── _files(auto-generated)
│      		├── config/
│       	│   	├── qa.properties
│       	│   	├── stage.properties
│       	│   	└── prod.properties
│      		├── payloads(empty)
│       	└── schemas/
│           		├── petstore/
│           		│	├── create-pet-response.json
│    			│	├── error-response.json
│    			│	├── find-by-status-response.json
│    			│	└── get-pet-response.json
│              	        ├── reqres/
│                   	│   	├── create-users-response.json
│       	        │       └── get-users-response.json
│              	        └── sample-image.jpg
│
├── Jenkinsfile
├── .gitignore
├── Project Structure
├── testng.xml
├── README.md
└── pom.xml
```

---

# Core Engineering Components

## Retry Engine

Capabilities:

• retries only idempotent requests
• exponential backoff strategy
• configurable retry policies

Purpose:

Handle **transient network failures** without creating flaky tests.

---

## Circuit Breaker

Each service maintains an independent circuit breaker.

States:

```
CLOSED
OPEN
HALF-OPEN
```

Benefits:

• prevents cascading failures
• protects unstable services
• simulates production safety patterns

---

## Chaos Injection

Chaos mode simulates unstable environments.

Example runtime flags:

```
-Dchaos.enabled=true
-Dchaos.failure.rate=0.3
-Dchaos.latency.ms=500
```

Capabilities:

• artificial latency
• random failures
• resilience testing

---

## Observability Layer

Each request is assigned a **correlation ID**.

Captured telemetry:

• service name
• response time
• request metadata
• response metadata

Example metrics output:

```
Service: GITHUB  | Calls: 20 | Avg: 1502 ms | Max: 3605 ms
Service: PETSTORE| Calls: 10 | Avg: 1780 ms | Max: 2467 ms
Service: REQRES  | Calls: 3  | Avg: 287 ms  | Max: 385 ms
```

Automation becomes a **diagnostic tool**, not just a validation suite.

---

# CI/CD Pipeline

```mermaid
flowchart LR

A[Developer Push] --> B[GitHub Repository]

B --> C[CI Trigger]

C --> D[Build Stage]

D --> E[Service Discovery]

E --> F{Service Matrix}

F --> G1[Reqres Tests]
F --> G2[Petstore Tests]
F --> G3[GitHub Tests]

G1 --> H[Parallel Execution]
G2 --> H
G3 --> H

H --> I[Test Results]

I --> J[Allure Report]

J --> K[Artifacts Stored]
```

Pipeline flow:

```
Code Push
 → Build
 → Service Discovery
 → Parallel Execution
 → Report Generation
 → Artifact Storage
```

---

# Running the Framework

### Basic Execution

```
mvn clean test -Denv=qa
```

---

### Run Smoke Tests

```
mvn clean test -Dgroups=smoke
```

---

### Run Specific Service

```
mvn clean test -Dservice=reqres
```

Multiple services:

```
mvn clean test "-Dservice=reqres,petstore"
```

---

### Chaos Mode Execution

```
mvn clean test -Denv=qa -Dchaos.enabled=true -Dchaos.failure.rate=0.3
```

---

# Allure Reporting

Generate report locally:

```
mvn allure:serve
```

CI stores reports in:

```
target/site/allure-maven-plugin
```

---

# Adding a New Service

Steps:

1. Add new **ServiceType enum**
2. Add configuration properties
3. Create a client extending **BaseApiClient**

Example:

```
PaymentsClient extends BaseApiClient
```

4. Configure resilience policies if required
5. Add tests inside

```
tests/payments
```

No core framework modification required.

---

# Engineering Principles

The framework follows:

• service isolation
• configuration-driven execution
• fail-fast configuration validation
• deterministic parallel execution
• observability-first automation
• resilience-aware testing

---

# Future Enhancements

Potential platform extensions:

• Prometheus metrics export
• Grafana dashboards
• distributed tracing
• memory leak detection
• YAML-based resilience configuration
• test impact analysis

---

# Author

Manoj Kumar
SDET | Automation Engineer

Technology Stack
Java | Rest Assured | TestNG | WireMock | Maven | Allure | CI/CD (GitHub Actions, Jenkins)

---
