CREATE TABLE IF NOT EXISTS hero
(
    id
    UUID
    NOT
    NULL
    PRIMARY
    KEY,
    name
    VARCHAR,
    auto_renewal
    BOOLEAN,
    created_by
    VARCHAR,
    updated_by
    VARCHAR,
    created_at
    TIMESTAMPTZ
    NOT
    NULL
    DEFAULT
    NOW
(
),
    deleted_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
    );

CREATE TABLE IF NOT EXISTS customer
(
    id
    UUID
    NOT
    NULL
    PRIMARY
    KEY,
    name
    VARCHAR,
    password
    VARCHAR,
    created_by
    VARCHAR,
    updated_by
    VARCHAR,
    created_at
    TIMESTAMPTZ
    NOT
    NULL
    DEFAULT
    NOW
(
),
    deleted_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
    );

CREATE TABLE IF NOT EXISTS student
(
    id
    UUID
    NOT
    NULL
    PRIMARY
    KEY,
    name
    VARCHAR,
    created_by
    VARCHAR,
    updated_by
    VARCHAR,
    created_at
    TIMESTAMPTZ
    NOT
    NULL
    DEFAULT
    NOW
(
),
    deleted_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
    );

CREATE TABLE IF NOT EXISTS authority
(
    id
    UUID
    NOT
    NULL
    PRIMARY
    KEY,
    customer_id
    UUID,
    role_id
    UUID,
    created_by
    VARCHAR,
    updated_by
    VARCHAR,
    created_at
    TIMESTAMPTZ
    NOT
    NULL
    DEFAULT
    NOW
(
),
    deleted_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
    );

CREATE TABLE IF NOT EXISTS role
(
    id
    int
    NOT
    NULL
    PRIMARY
    KEY,
    user_id
    int,
    name
    VARCHAR,
    created_by
    VARCHAR,
    updated_by
    VARCHAR,
    created_at
    TIMESTAMPTZ
    NOT
    NULL
    DEFAULT
    NOW
(
),
    deleted_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
    );

CREATE TABLE IF NOT EXISTS users
(
    id
    int
    NOT
    NULL
    PRIMARY
    KEY,
    name
    VARCHAR,
    password
    VARCHAR,
    created_by
    VARCHAR,
    updated_by
    VARCHAR,
    created_at
    TIMESTAMPTZ
    NOT
    NULL
    DEFAULT
    NOW
(
),
    deleted_at TIMESTAMPTZ,
    updated_at TIMESTAMPTZ
    );

CREATE
EXTENSION "uuid-ossp";
insert into role (id, name, created_by, updated_by, created_at, updated_at)
values (uuid_generate_v4(), 'SUPER_ADMIN', '1', '1', now(), now());
insert into role (id, name, created_by, updated_by, created_at, updated_at)
values (uuid_generate_v4(), 'ADMIN', '1', '1', now(), now());
insert into role (id, name, created_by, updated_by, created_at, updated_at)
values (uuid_generate_v4(), 'VIP1', '1', '1', now(), now());
insert into role (id, name, created_by, updated_by, created_at, updated_at)
values (uuid_generate_v4(), 'NORMAL', '1', '1', now(), now());