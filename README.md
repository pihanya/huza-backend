Dev Env

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
# huza-backend
