# Minibank Application

### Tech Stack
- Http Server and Framework: Spring Boot
- Java version: 17
- Builder: Maven
- Containerized Database: MySQL8
- ORM: Hibernate from Spring Boot

### Technical Specifications and Limitations
- `BigDecimal` is used here to precisely compute the reversals of deposits and withdrawals. This is to avoid the floating point issues programming languages have.
- Pagination feature are only applied to the list of transaction endpoint since this resource usually have more records than others.
- For the currency list and conversion I've used a third-party API from `currencybeacon.com`. They have free-tier APIs limited only for 5000 calls per day.
- Authentication and Authorization are not part of the system yet. However, users can have an admin role to have control on transaction reversal.
- API calls are in `block mode` meaning it is synchronous, and it can affect the performance of the application.
- Data coming from the database are not yet streamed, so it may have performance issues on huge records.
- Tests modules are not yet included in the application.
- Deletion is only available for users, provided that the user does not have any bank accounts.
- No soft deletes yet for all entities (users, bank accounts, transactions).
- Validations currently will escalate errors to `5XX` type errors even though the error is just `Bad Request`.
- Environment variables for third-party libraries and database are not yet secured. 

### Design Choices
- The application manages three(3) database tables, one that holds registered users, another that holds the bank accounts for each user and the last one holds transactions for each bank accounts.
- Users can have as many bank accounts as they want, provided that their name matches the holder name of their bank account.
- The application has two(2) main packages to handle entities like `user` and `bank`. The user holds the actions like deposit, withdrawal and reversal transactions while the bank holds the bank accounts of users, list of currencies and conversions.
- The Bank class is a singleton than can only be instantiated once. The benefit of having a singleton here is the list of currency API will be only called once. 
- The application applies factory design pattern for the various types of transactions such as deposit and withdrawal transactions as well as the handling of reversal of transactions.
- Applied a locking mechanism when reversal is applied to a certain transaction. It can be unlocked by a user with an admin role so that the user can again do a reversal of a transaction. Once the transaction has been reversed though, it cannot be reversed the second time.

### How to run the Application
Assumption: The application is now cloned or forked to your local machine.
1. Go to the docker directory `src/main/docker` and issue the following command. This will create a container for your MySQL database. Be sure you have `docker` and `docker-compose` installed on your local machine before issuing the command.
    ```bash
   docker-compose up -d
    ```
2. (Optional) You may test the database connection by having any `MySQL` client.
3. Issue the command to spin up the server.
   - Dependencies:
   - You need to make sure that the `MySQL` server container from your docker is already up and running.
   - After that, go to `target` directory and check the version of your `JDK`. It is recommended that you should have at least version 17 of the `JDK` and up.
      ```bash
      java -jar demo-0.0.1-SNAPSHOT.jar --server.port=8080
      ```
4. (Optional) You may use `IntelliJ IDE` to build and run the application on your local machine.
    - Dependencies:
    - You still need to run the `MySQL` server container using `docker-compose`.

### Endpoints
#### Ping server
```bash
curl --request GET \
  --url http://localhost:8080/api/health \
  --header 'User-Agent: insomnia/2023.5.8'
```
#### User registration endpoints
- Register new user using their name and email address.
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/users \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/2023.5.8' \
  --data '{
	"name": "John Doe",
	"username": "john.doe@gmail.com"
}'
```
- (Optional) Register an admin user with the same parameter for registering users
```bash
curl --request POST \
  --url 'http://localhost:8080/api/v1/users?isAdmin=true' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/2023.5.8' \
  --data '{
	"name": "Admin",
	"username": "admin@domain.com"
}'
```

- Get all active users
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/users \
  --header 'User-Agent: insomnia/2023.5.8'
```

- Get a specific user by using their ID
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/users/8 \
  --header 'User-Agent: insomnia/2023.5.8'
```
- You can update the user's information by using the user's ID
```bash
curl --request PUT \
  --url 'http://localhost:8080/api/v1/users/8?isAdmin=true' \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/2023.5.8' \
  --data '{
	"name": "John Doe",
	"username": "doe.john@gmail.com",
}'
```
- You can delete the user by using their user's ID
```bash
curl --request DELETE \
  --url http://localhost:8080/api/v1/users/8 \
  --header 'User-Agent: insomnia/2023.5.8'
```

#### Bank account endpoints
- Get user's bank accounts
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/users/8/bankaccounts \
  --header 'User-Agent: insomnia/2023.5.8'
```
- Get list of available currencies
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/bank/currencies \
  --header 'User-Agent: insomnia/2023.5.8' 
```
- Register new bank account to a user with the current currency of the account.
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/users/8/bankaccounts \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/2023.5.8' \
  --data '{
	"holderName": "John Doe",
	"currency": "PHP"
}'
```
- Get user's specific bank account by using the bank account number
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/users/8/bankaccounts/d4b29d0e-5b8b-4f8e-b74a-77da30c62c1c \
  --header 'User-Agent: insomnia/2023.5.8'
```

#### Transactional endpoints
- Deposit funds to specific bank account of a user. Note that the currency here is optional. This endpoint is capable to syncing up the amount from the entered currency to the current currency of the bank account.
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/users/8/bankaccounts/d4b29d0e-5b8b-4f8e-b74a-77da30c62c1c/deposit \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/2023.5.8' \
  --data '{
	"amount": 8000.00,
	"currency": "AUD"
}'
```
- Withdraw funds to specific bank account of a user. Same as the deposit endpoint, the currency is optional. It can also sync up the amount from the entered currency to the current currency of the bank account.  
```bash
curl --request POST \
  --url http://localhost:8080/api/v1/users/8/bankaccounts/d4b29d0e-5b8b-4f8e-b74a-77da30c62c1c/withdraw \
  --header 'Content-Type: application/json' \
  --header 'User-Agent: insomnia/2023.5.8' \
  --data '{
	"amount": 500
}'
```
- Get a specific bank account details of a user by using the bank account number.
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/users/8/bankaccounts/d4b29d0e-5b8b-4f8e-b74a-77da30c62c1c \
  --header 'User-Agent: insomnia/2023.5.8' 
```
- Get all active bank accounts of a user
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/users/8/bankaccounts \
  --header 'User-Agent: insomnia/2023.5.8' 
```
- Get transaction history sorted in descending order by transaction timestamp. The endpoint also provides facility to paginate its responses by providing page and limit parameter to the `URL`.
```bash
curl --request GET \
  --url 'http://localhost:8080/api/v1/users/8/bankaccounts/d4b29d0e-5b8b-4f8e-b74a-77da30c62c1c/transactions?page=1&limit=10' \
  --header 'User-Agent: insomnia/2023.5.8' 
```
- Get a specific transaction of a user by using a transaction number to view the transaction details.
```bash
curl --request GET \
  --url http://localhost:8080/api/v1/users/8/bankaccounts/d4b29d0e-5b8b-4f8e-b74a-77da30c62c1c/transactions/0423ccd3-5e0d-43b3-98a3-684f1303f016 \
  --header 'User-Agent: insomnia/2023.5.8' 
```
- Reverse deposit transactions using the transaction number of a deposit transaction. Once the transaction has been reversed, the user cannot perform another transaction reversal. In addition, the user's bank account will locked for reversal and may only be release by an admin user.
```bash
curl --request DELETE \
  --url http://localhost:8080/api/v1/users/8/bankaccounts/d4b29d0e-5b8b-4f8e-b74a-77da30c62c1c/transactions/665a07b7-ba2e-4beb-9ba7-9b01ca922f3f/deposit \
  --header 'User-Agent: insomnia/2023.5.8'
```
- Reverse withdrawal transaction using the transaction number of a withdrawal transaction. Same reversal process as the deposit, transactions that are already reversed will not be reversed the second time. In addition, bank accounts link to the transaction will also be locked and admin users can only unlock the account.
```bash
curl --request DELETE \
  --url http://localhost:8080/api/v1/users/8/bankaccounts/d4b29d0e-5b8b-4f8e-b74a-77da30c62c1c/transactions/d391763a-2298-47d1-9007-e2db67672abc/withdraw \
  --header 'User-Agent: insomnia/2023.5.8'
```
- Unlock bank account to do reversal after careful inspection or reconciliation process of the admin user.
```bash
curl --request POST \
  --url 'http://localhost:8080/api/v1/users/8/bankaccounts/d4b29d0e-5b8b-4f8e-b74a-77da30c62c1c/reconcile?adminId=8' \
  --header 'User-Agent: insomnia/2023.5.8' 
```

