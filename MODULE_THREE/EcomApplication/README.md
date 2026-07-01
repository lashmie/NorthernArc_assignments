# EcomApplication

Spring Boot REST API for customer, product, and order workflows.

## Implemented use cases

- Customer search by name
- Product search by name, category, and brand
- Customer places an order with one or more products

## Main endpoints

### Customers

- `POST /api/customers`
- `GET /api/customers`
- `GET /api/customers/{id}`
- `GET /api/customers/search?name=an`
- `DELETE /api/customers/{id}`

### Products

- `POST /api/products`
- `GET /api/products`
- `GET /api/products/{id}`
- `GET /api/products/search?name=phone&category=electronics&brand=samsung`
- `GET /api/products/search/by-name?name=phone`
- `GET /api/products/search/by-category?category=electronics`
- `GET /api/products/search/by-brand?brand=samsung`
- `DELETE /api/products/{id}`

### Orders

- `POST /api/orders/place`
- `POST /api/orders/customer/{customerId}/place`
- `POST /customer/orders/place`
- `GET /api/orders`
- `GET /api/orders/{orderId}`
- `GET /api/orders/customer/{customerId}`
- `GET /api/orders/search/by-customer-name?customerName=an`
- `DELETE /api/orders/{orderId}`

## Place order payload

```json
{
  "customerId": 1,
  "items": [
    {
      "productId": 2,
      "quantity": 1
    },
    {
      "productId": 3,
      "quantity": 2
    }
  ]
}
```

## Run locally

```powershell
.\mvnw.cmd spring-boot:run
```

## Run tests

```powershell
.\mvnw.cmd test
```

