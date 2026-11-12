# Member Service

Part of the **Library Management Microservices System** (SEZG583 Scalable Services, BITS Pilani WILP).

| | |
|---|---|
| Author | Sivesh Kumar (2025MT03074) |
| Port | 8081 |
| Stack | Java 17, Spring Boot 3.2, Spring Data JPA, H2, Maven, Docker, Kubernetes |
| Companion repo | `book-service` (calls this service over REST) |

## What it does
Owns all library member data: create, read, update and delete members. The Book Service calls
`GET /api/members/{id}` to confirm that a member exists and to read the membership type
(STANDARD or PREMIUM), which decides how many books the member may hold.

## REST API
| Method | URL | Description | Success |
|---|---|---|---|
| POST | `/api/members` | Create member | 201 |
| GET | `/api/members` | Get all members | 200 |
| GET | `/api/members/{id}` | Get one member | 200 / 404 |
| PUT | `/api/members/{id}` | Update member | 200 / 404 / 409 |
| DELETE | `/api/members/{id}` | Delete member | 204 / 404 |

Body (POST / PUT):
```json
{ "fullName": "Anitha Raman", "email": "anitha.raman@example.com", "phone": "9876543210", "membershipType": "STANDARD" }
```
Errors: 400 (validation), 404, 409 (duplicate email). Health: `/actuator/health`. H2 console: `/h2-console` (`jdbc:h2:mem:memberdb`, user `sa`).

## Prerequisites
Java 17+, Maven 3.8+, Docker Desktop.

## Run locally
```bash
mvn clean package
java -jar target/member-service.jar
curl http://localhost:8081/actuator/health
```

## Docker
```bash
mvn clean package
docker build -t member-service:1.0 .
docker network create lib-net
docker run -d --name member-service --network lib-net -p 8081:8081 member-service:1.0
docker logs member-service
```

## Kubernetes
```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/member-service-deployment.yaml
kubectl apply -f k8s/member-service-service.yaml
kubectl get pods -n library-system
kubectl port-forward -n library-system svc/member-service 8081:8081
```
Optional: `kubectl apply -f k8s/member-service-hpa.yaml` (needs metrics-server).

## Project structure
```
member-service/
├── .github/workflows/ci.yml
├── .gitignore
├── Dockerfile
├── pom.xml
├── README.md
├── k8s/  (namespace, deployment, service, hpa)
└── src/
    ├── main/java/com/bits/library/member/
    │   ├── MemberServiceApplication.java
    │   ├── controller/MemberController.java
    │   ├── service/MemberService.java
    │   ├── repository/MemberRepository.java
    │   ├── model/{Member, MembershipType}.java
    │   ├── dto/{MemberRequest, MemberResponse}.java
    │   └── exception/{NotFoundException, ConflictException, ApiError, GlobalExceptionHandler}.java
    ├── main/resources/application.properties
    └── test/java/com/bits/library/member/MemberServiceApplicationTests.java
```

## Known limitation
H2 is in memory: data is lost on restart and not shared between replicas.
