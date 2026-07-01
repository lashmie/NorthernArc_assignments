# Loan Management API

Spring Boot API implementing loan approval, EMI schedule generation, repayment tracking, overdue penalty handling, and dashboard reporting.

## Implemented Tasks

- Task 1: JPA mapping with `@OneToMany`, `@ManyToOne`, `cascade`, `fetch`, `mappedBy`, and `orphanRemoval`
- Task 2: Bean Validation on entities and request DTOs
- Task 3: Derived queries for loan type, city, status, overdue, and principal filters
- Task 4: Complex JPQL queries for overdue and dashboard analytics
- Task 5: `@Modifying` JPQL query to revise annual interest rate by loan type
- Task 6: Pagination and sorting for `GET /loans` by `principalAmount DESC`
- Task 7: API responses with `LoanSummaryDTO` and `LoanDashboardDTO`

## Main Endpoints

- `POST /loans/approve`
- `POST /loans/emi/pay`
- `GET /loans?page=0&size=10`
- `GET /loans/dashboard`
- `PATCH /loans/interest-rate?loanType=PERSONAL&newRate=13.5`
- `GET /loans/search/by-type?loanType=PERSONAL`
- `GET /loans/search/by-city?city=Chennai`
- `GET /loans/search/by-status?status=ACTIVE`
- `GET /loans/search/by-principal?amount=100000`

## Quick Run

```powershell
Set-Location "C:\Guvi_Learning\MODULE_4\day4\Revision\Loan"
.\mvnw.cmd spring-boot:run
```

## Quick Test

```powershell
Set-Location "C:\Guvi_Learning\MODULE_4\day4\Revision\Loan"
.\mvnw.cmd test
```

