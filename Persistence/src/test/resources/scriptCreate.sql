create table "Users"
(
    "id"                bigserial
        primary key,
    "firstName"       varchar not null,
    "lastName"        varchar not null,
    "personalNumber"  varchar not null,
    "phoneNumber"     varchar not null,
    "password"          varchar not null,
    "email"             varchar not null,
    "birthday"          date    not null,
    "country"           varchar,
    "county"            varchar,
    "city"              varchar,
    "street"            varchar,
    "number"            varchar,
    "apartment"         varchar,
    "investedAmount"  double precision,
    "availableAmount" double precision,
    "usedAmount"      double precision
);

create table "DebitCards"
(
    "id"              bigserial
        primary key,
    "debitCardType" varchar not null,
    "cardNumber"    varchar not null,
    "cvv"             varchar not null,
    "expireDate"    date    not null,
    "owner"           bigint
        constraint "userId"
            references "Users"
);

create table "Stocks"
(
    "id"            bigserial
        primary key,
    "name"          varchar
        unique,
    "companyName" varchar
);

create table "StockPrices"
(
    "stock" bigint not null
        references "Stocks",
    "date"  date   not null,
    "price" double precision,
    primary key ("stock", "date")
);

create table "Sales"
(
    "stock"  bigint
        references "Stocks",
    "user" bigint
        constraint "Sales_Users_id_fk"
            references "Users",
    "id"     bigserial
        primary key,
    "date"   date,
    "time"   time,
    "sum"    double precision
);

create table "Purchases"
(
    "stock"  bigint
        references "Stocks",
    "user" bigint
        constraint "Purchases_Users_id_fk"
            references "Users",
    "id"     bigserial
        primary key,
    "date"   date,
    "time"   time,
    "sum"    double precision
);

