# Employee API (Simple CRUD)

This project now includes a complete Employee REST API with:
- `id`
- `name`
- `salary`

## Endpoints

- `POST /api/employees` - create employee
- `GET /api/employees` - list all employees
- `GET /api/employees/{id}` - get one employee by id
- `PUT /api/employees/{id}` - update employee
- `DELETE /api/employees/{id}` - delete employee

## Sample Request Body

```json
{
  "name": "Alice",
  "salary": 45000.00
}
```

## Quick Run

```powershell
./mvnw spring-boot:run
```

## Quick Test

```powershell
./mvnw test
```

