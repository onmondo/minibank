# Minibank Application

### Tech Stack
- Http Server: Spring Boot
- Java version: 17
- Builder: Maven
- Containerized Database: MySQL8
- ORM: Hibernate from Sprint Boot

### Specificatoins
- I used the BigDecimal to precisely compute the reversals of deposits and withdrawals. This is to avoid the floating point issues programming languages have.
- For the currency list and conversion I've used a third-party API from `currencybeacon.com`. They have free-tier APIs limited only for 5000 calls per day.