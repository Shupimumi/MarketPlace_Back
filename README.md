# MarketPlace_Back

Запуск keycloak:


Запуск Postgre:
docker run --name marketplace-pg -e POSTGRES_PASSWORD={pass} -v pgdata-mp:/var/lib/postgresql -p 5432:5432 -e POSTGRES_DB=marketplace -d postgres:18



Запуск 