# huza-backend

## Local Setup

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
PROJECT_VERSION=$(grep 'version=' gradle.properties | sed 's/.*version=\(.*\)/\1/')
java -jar build/huza-app/libs/huza-app-$PROJECT_VERSION.jar \
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

## Deployment

Change `scripts/.env`. It contains:
- `HUZA_PORT` — port that backend started on. also used by frontend (default `4242`)
- `REMOTE_HOST` — host used by frontend to access backend (default `localhost`)
- `HELIOS_USER` — user on Helios server (default `s244701`)

Build frontend into resources directory. On multiple runs flag `--cache` can be used:

```shell
scripts/build_frontend.sh --remote
```

Build backend and deploy to Helios:

```shell
scripts/deploy_to_helios.sh
```
