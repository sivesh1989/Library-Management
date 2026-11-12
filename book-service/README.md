# Book Service

Part of the **Library Management Microservices System** (SEZG583 Scalable Services, BITS Pilani WILP).

| | |
|---|---|
| Author | Sivesh Kumar (2025MT03074) |
| Port | 8082 |
| Stack | Java 17, Spring Boot 3.2, Spring Data JPA, H2, RestClient, Maven, Docker, Kubernetes |
| Depends on | `member-service` (REST, URL from `MEMBER_SERVICE_URL`) |

## What it does
Owns the book catalogue and the loans. To borrow a book it checks the book, calls the Member Service to
check the member, then applies the rules: no second loan of the same book, a free copy must exist, and the member
must be under the loan limit (STANDARD 3, PREMIUM 5). Returning a book closes the loan and puts the copy back.

## REST API
| Method | URL | Description | Success |
|---|---|---|---|
| POST | `/api/books` | Add book | 201 |
| GET | `/api/books` | Get all books | 200 |
| GET | `/api/books/{id}` | Get one book | 200 / 404 |
| POST | `/api/loans` | Borrow a book | 201 / 404 / 409 / 503 |
| PUT | `/api/loans/{loanId}/return` | Return a book | 200 / 404 / 409 |
| GET | `/api/loans` | View all loans | 200 |
| GET | `/api/loans/{loanId}` | View one loan | 200 / 404 |
| GET | `/api/loans/member/{memberId}` | View loans of a member | 200 |

Book body: `{ "isbn": "9781617294549", "title": "Microservices Patterns", "author": "Chris Richardson", "copies": 1 }`
Borrow body: `{ "memberId": 1, "bookId": 1 }`

## Configuration
| Property / env variable | Default | Meaning |
|---|---|---|
| `MEMBER_SERVICE_URL` | `http://localhost:8081` | Base URL of the Member Service |
| `library.loan-days` | 14 | Loan period |
| `library.max-loans.standard` / `.premium` | 3 / 5 | Active loan limits |

## Run locally
```bash
mvn clean package
java -jar target/book-service.jar
```

## Docker
```bash
mvn clean package
docker build -t book-service:1.0 .
docker network create lib-net        # skip if it exists
docker run -d --name book-service --network lib-net -p 8082:8082 \
  -e MEMBER_SERVICE_URL=http://member-service:8081 book-service:1.0
docker logs book-service
```

## Kubernetes
```bash
kubectl apply -f k8s/namespace.yaml
kubectl apply -f k8s/book-service-configmap.yaml
kubectl apply -f k8s/book-service-deployment.yaml
kubectl apply -f k8s/book-service-service.yaml
kubectl get pods -n library-system
kubectl port-forward -n library-system svc/book-service 8082:8082
```

## Project structure
```
book-service/
├── .github/workflows/ci.yml
├── .gitignore
├── Dockerfile
├── pom.xml
├── README.md
├── k8s/  (namespace, configmap, deployment, service, hpa)
└── src/
    ├── main/java/com/bits/library/book/
    │   ├── BookServiceApplication.java
    │   ├── client/MemberClient.java
    │   ├── config/{LibraryRules, RestClientConfig}.java
    │   ├── controller/{BookController, LoanController}.java
    │   ├── service/{BookService, LoanService}.java
    │   ├── repository/{BookRepository, LoanRepository}.java
    │   ├── model/{Book, Loan}.java
    │   ├── dto/{BookRequest, BookResponse, BorrowRequest, LoanResponse, MemberDto}.java
    │   └── exception/{NotFoundException, ConflictException, MemberServiceUnavailableException, ApiError, GlobalExceptionHandler}.java
    ├── main/resources/application.properties
    └── test/java/com/bits/library/book/BookServiceApplicationTests.java
```

## Known limitation
H2 is in memory (data lost on restart, not shared across replicas). Two simultaneous borrow requests for the last
copy are not locked against each other.
