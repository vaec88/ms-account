## Stack de tecnologías:
* Java 21
* Spring Boot 4.0.6
* Spring WebFlux 7.0.7
* PostgreSQL 12
* R2DBC

## Ruta de archivos SQL para crear la base de datos y tablas:
* src\main\resources\sql\init.sql

## Especificación OPENAPI:
http://localhost:8090/swagger-ui.html


## Crear cuentas de prueba:

```json
{
  "number":"474747",
  "type":"SAVINGS_ACCOUNT",
  "initialBalance":2000,
  "status":true,
  "customerId":1
}
```

```json
{
  "number":"225225",
  "type":"CHECKING_ACCOUNT",
  "initialBalance":100,
  "status":true,
  "customerId":2
}
```

```json
{
  "number":"494949",
  "type":"SAVINGS_ACCOUNT",
  "initialBalance":0,
  "status":true,
  "customerId":3
}
```

```json
{
  "number":"496496",
  "type":"SAVINGS_ACCOUNT",
  "initialBalance":540,
  "status":true,
  "customerId":2
}
```

```json
{
   "number":"585858",
   "type":"CHECKING_ACCOUNT",
   "initialBalance":1000,
   "status":true,
   "customerId":1
}
```

## Crear movimientos de prueba:

```json
{
  "amount":575,
  "accountNumber":"474747",
  "movementType":"DEBIT"
}
```

```json
{
  "amount":600,
  "accountNumber":"225225",
  "movementType":"CREDIT"
}
```

```json
{
  "amount":150,
  "accountNumber":"494949",
  "movementType":"CREDIT"
}
```

```json
{
  "amount":540,
  "accountNumber":"496496",
  "movementType":"DEBIT"
}
```