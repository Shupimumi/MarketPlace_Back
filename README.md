# MarketPlace_Back

Запуск keycloak:


Запуск PG:
docker run --name marketplace-pg -e POSTGRES_PASSWORD={pass} -v pgdata-mp:/var/lib/postgresql -p 5432:5432 -e POSTGRES_DB=marketplace -d postgres:18
Взаимодействие с PG:
docker exec -it <имя> psql -U postgres -d postgres:18


## Схема БД в формате ER-диаграммы

Текущее состояние JPA-сущностей (`db/entities`).

```mermaid
erDiagram
    %% ===== catalog =====
    GOOD {
        bigint  id PK
        varchar full_name "NOT NULL"
        varchar short_name
        numeric cost "NOT NULL"
    }

    %% ===== client =====
    CLIENT {
        bigint  id PK
        varchar name "NOT NULL"
        varchar surname
        varchar address
    }

    %% ===== cart =====
    CART {
        bigint id PK
        bigint client_id UK "только id → client (1:1)"
    }
    CART_ITEM {
        bigint id PK
        bigint cart_id FK
        bigint good_id "только id → good"
        int    quantity
    }

    %% ===== order =====
    CUSTOMER_ORDER {
        bigint  id PK
        bigint  client_id "только id → client"
        varchar status "IN_PROGRESS / COMPLETED / CANCELLED"
        varchar description
    }
    CUSTOMER_ORDER_ITEM {
        bigint  id PK
        bigint  customer_order_id FK
        bigint  good_id "только id → good"
        varchar good_name "снимок на момент заказа"
        numeric cost "цена на момент заказа"
        int     quantity
    }

    %% ===== payment =====
    ACCOUNT {
        bigint  id PK
        bigint  client_id UK "только id → client (1:1)"
        numeric balance "NOT NULL"
    }
    PAYMENT {
        bigint    id PK
        bigint    account_id "только id → account"
        bigint    customer_order_id "только id → customer_order"
        numeric   amount "NOT NULL"
        varchar   status "CREATED / PROCESSING / COMPLETED / FAILED"
        timestamp created_at "@PrePersist, updatable = false"
    }

    %% FK внутри агрегата (сплошная линия)
    CART           ||--o{ CART_ITEM           : "1:N"
    CUSTOMER_ORDER ||--|{ CUSTOMER_ORDER_ITEM : "1:N"

    %% только id между агрегатами (пунктир)
    GOOD           ||..o{ CART_ITEM           : "good_id"
    GOOD           ||..o{ CUSTOMER_ORDER_ITEM : "good_id"
    CLIENT         ||..o| CART                : "client_id"
    CLIENT         ||..o{ CUSTOMER_ORDER      : "client_id"
    CLIENT         ||..o| ACCOUNT             : "client_id"
    ACCOUNT        ||..o{ PAYMENT             : "account_id"
    CUSTOMER_ORDER ||..o{ PAYMENT             : "customer_order_id (попытки оплаты)"
```

Планируется (ещё нет в сущностях):

- `CLIENT.keycloak_sub` (UK) — `sub` из JWT;
- `CUSTOMER_ORDER.created_at`, `total` (хранить или считать — открытый вопрос);
- `ACCOUNT.version` — `@Version`, оптимистичная блокировка конкурентных списаний;
- не больше одного `COMPLETED`-платежа на заказ — проверка в сервисе
  или частичный индекс `UNIQUE (customer_order_id) WHERE status = 'COMPLETED'` в миграции.


Запуск 