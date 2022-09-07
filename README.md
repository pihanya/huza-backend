# huza-backend

Use Java 17

Setup database:
```shell
docker run -d \
    --name huza-pgsql \
    -e POSTGRES_USER=huza \
    -e POSTGRES_PASSWORD=huza \
    -e POSTGRES_DB=huza \
    -e PGDATA=/var/lib/postgresql/data/pgdata \
    -p 5432:5432 \
    postgres:14.2
```


Build:

```shell
./gradlew build
```

Run in dev env:

```shell
java -jar build/libs/huza-0.0.1-SNAPSHOT.jar \
    --spring.profiles.active=dev
```

Test in Dev env:

```shell
curl --location --request POST '127.0.0.1:4242/api/auth/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "email": "admin@itmo.ru",
    "password": "password"
}'
```
