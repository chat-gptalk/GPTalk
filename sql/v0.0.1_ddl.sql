create table tenants
(
    id            serial primary key,
    tenant_id     uuid        not null unique default gen_random_uuid(),
    name          text        not null,
    owner_user_id uuid        not null,
    status        integer     not null        default 1, -- entity status
    created_at    timestamptz not null        default current_timestamp,
    updated_at    timestamptz not null        default current_timestamp
);

create table users
(
    id           serial primary key,
    user_id      uuid        not null unique default gen_random_uuid(),
    tenant_id    uuid        not null,
    username     text unique not null,
    password     text        not null,
    account_type text        not null        default 'MAIN', -- MAIN, SUB
    roles        text[]      not null,
    status       integer     not null        default 1,
    created_at   timestamptz not null        default current_timestamp,
    updated_at   timestamptz not null        default current_timestamp
);

create table api_keys
(
    id             serial primary key,
    user_id        uuid        not null,
    tenant_id      uuid        not null,
    api_key_id     uuid        not null unique default gen_random_uuid(),
    key_hash       text        not null,
    name           text        not null,
    allowed_models text[],
    status         integer     not null        default 1,
    expire_at      timestamptz,
    created_at     timestamptz not null        default current_timestamp,
    updated_at     timestamptz not null        default current_timestamp
);

create table virtual_models
(
    id               serial primary key,
    user_id          uuid        not null,
    tenant_id        uuid        not null,
    virtual_model_id uuid        not null unique default gen_random_uuid(),
    name             text        not null unique, -- e.g. 'vm:gpt-4', 'vm:embedding'
    description      text,
    enabled          boolean     not null        default true,
    created_at       timestamptz not null        default current_timestamp,
    updated_at       timestamptz not null        default current_timestamp
);
create table virtual_model_mappings
(
    id               serial primary key,
    user_id          uuid    not null,
    tenant_id        uuid    not null,
    virtual_model_id uuid    not null,
    model_id         uuid    not null, -- maps to llm_models.model_id
    priority         integer not null default 0,
    weight           integer not null default 1,
    unique (virtual_model_id, model_id)
);


create table llm_providers
(
    id               serial primary key,
    user_id          uuid        not null, -- system when system=true
    tenant_id        uuid        not null, -- system when system=true
    provider_id      uuid        not null unique default gen_random_uuid(),
    name             text        not null unique,
    base_url         text        not null,
    description      text        null,
    sdk_client_class text        not null        default 'openai',
    system           boolean     not null        default true,
    enabled          boolean     not null        default true,
    extra_config     jsonb,
    created_at       timestamptz not null        default now(),
    updated_at       timestamptz not null        default now()
);

create table llm_keys
(
    id          serial primary key,
    user_id     uuid        not null,
    tenant_id   uuid        not null,
    llm_key_id  uuid        not null unique default gen_random_uuid(),
    provider_id uuid        not null,
    llm_key     text        not null,
    name        text        not null unique,
    priority    integer     not null        default 0,
    active      boolean     not null        default true,
    created_at  timestamptz not null        default now(),
    updated_at  timestamptz not null        default now()
);

create table llm_models
(
    id                serial primary key,
    user_id           uuid        not null,
    tenant_id         uuid        not null,
    model_id          uuid        not null unique default gen_random_uuid(),
    provider_id       uuid        not null,
    name              text        not null,
    description       text        null,
    features          text[]      not null, -- chat, embedding, vision, audio_transcription, audio_generation, tool_calling, response_format
    context_length    integer     not null        default 0,
    max_output_tokens integer     not null        default 0,
    enabled           boolean     not null        default true,
    status            text        not null        default 'HEALTHY',
    default_params    jsonb,-- top_n ...
    last_checked_at   timestamptz,
    created_at        timestamptz not null        default now(),
    updated_at        timestamptz not null        default now(),
    unique (provider_id, name)
);


create table llm_model_pricing
(
    id             serial primary key,
    user_id        uuid           not null,
    tenant_id      uuid           not null,
    model_id       uuid           not null,
    modality       text           not null, -- 'text', 'image', 'audio', etc.
    usage_unit     text           not null, -- e.g. 'tokens', 'seconds', 'image_1024x1024', 'bytes'
    price_per_unit numeric(10, 6) not null, -- usd
    currency       text        default 'usd',
    effective_from timestamptz default now()
);

create table llm_logs
(
    id                bigserial primary key,
    user_id           uuid    not null,
    tenant_id         uuid    not null,
    llm_key_id        uuid    not null,
    model_id          uuid    not null,
    virtual_model_id  uuid    not null,
    api_key_id        uuid    not null,
    end_user_id       text    null,

    prompt_tokens     integer not null,
    cached_tokens     integer not null,
    completion_tokens integer not null,
    total_tokens      integer not null,

    function_calls    integer not null,

    input_characters  integer not null,
    response_format   text, -- json, text, image_url

    cached            boolean not null default false,
    stream            boolean not null default false,

    image_count       integer,
    image_resolution  text, -- e.g., '1024x1024'
    image_bytes       bigint,
    audio_duration_ms integer,

    request_data      json,
    response_data     json,
    response_status   integer not null default 0,

    duration_ms       integer not null default 0,

    cost              double precision,
    created_at        timestamptz      default now()
);

create table rate_limits
(
    id          serial primary key,
    user_id     uuid    not null,
    tenant_id   uuid    not null,
    target_type text    not null check (target_type in ('user', 'llm_key', 'api_key', 'model')),
    target_id   uuid    not null,

    window_type text    not null default 'fixed', -- fixed, sliding

    window_unit text    not null check (window_unit in ('second', 'minute', 'hour', 'day')),
    window_size integer not null,                 -- eg: 10 seconds, 1 hour, 1 day

    rpm         integer,                          -- rpm/rph
    max_tokens  integer,                          -- tpm/tph
    max_budget  decimal(10, 4),                   -- cost usd

    description text,

    created_at  timestamp        default current_timestamp,
    updated_at  timestamp        default current_timestamp
);
