# MarketPlace_Back

Запуск keycloak:


Запуск PG:
docker run --name marketplace-pg -e POSTGRES_PASSWORD={pass} -v pgdata-mp:/var/lib/postgresql -p 5432:5432 -e POSTGRES_DB=marketplace -d postgres:18
Взаимодействие с PG:
docker exec -it <имя> psql -U postgres -d postgres:18


## Схема БД в формате ER-диаграммы

```mermaid
erDiagram
    %% ===== catalog =====
    GOOD {
        bigint  id PK
        varchar pre_name
        varchar post_name
        numeric cost
    }

    %% ===== client =====
    CLIENT {
        bigint  id PK
        varchar keycloak_sub UK "sub из JWT"
        varchar name
        varchar surname
        varchar address
    }

    %% ===== order =====
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
    CUSTOMER_ORDER {
        bigint    id PK
        bigint    client_id "только id → client"
        varchar   status "NEW / PAID / PAYMENT_FAILED / CANCELLED"
        numeric   total "(?) хранить или считать"
        timestamp created_at
    }
    ORDER_ITEM {
        bigint  id PK
        bigint  order_id FK
        bigint  good_id "только id → good"
        int     quantity
        numeric price "цена на момент заказа"
    }

    %% ===== payment =====
    ACCOUNT {
        bigint  id PK
        bigint  client_id UK "только id → client (1:1)"
        numeric balance
        bigint  version "@Version — оптимистичная блокировка"
    }
    PAYMENT {
        bigint    id PK
        bigint    account_id FK
        bigint    order_id UK "только id → order; (?) UNIQUE = 1:1"
        numeric   amount
        varchar   status
        timestamp created_at
    }

    %% FK внутри модуля (сплошная линия)
    CART           ||--o{ CART_ITEM  : "1:N"
    CUSTOMER_ORDER ||--|{ ORDER_ITEM : "1:N"
    ACCOUNT        ||--o{ PAYMENT    : "1:N"

    %% только id между модулями (пунктир)
    GOOD           ||..o{ CART_ITEM      : "good_id"
    GOOD           ||..o{ ORDER_ITEM     : "good_id"
    CLIENT         ||..o| CART           : "client_id"
    CLIENT         ||..o{ CUSTOMER_ORDER : "client_id"
    CLIENT         ||..|| ACCOUNT        : "client_id"
    CUSTOMER_ORDER ||..o| PAYMENT        : "order_id (?)"
```


Запуск 