CREATE TABLE _users
(
    id           UUID                        NOT NULL,
    full_name    VARCHAR(255),
    username     VARCHAR(255),
    email        VARCHAR(255),
    phone        VARCHAR(255),
    password     VARCHAR(255),
    role         VARCHAR(255)                NOT NULL,
    created_by   UUID                        NOT NULL,
    created_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    enabled      BOOLEAN                     NOT NULL,
    locked       BOOLEAN                     NOT NULL,
    CONSTRAINT pk__users PRIMARY KEY (id)
);

CREATE TABLE access_token
(
    id      UUID NOT NULL,
    token   VARCHAR(255),
    revoked BOOLEAN,
    user_id UUID NOT NULL,
    CONSTRAINT pk_access_token PRIMARY KEY (id)
);

CREATE TABLE data_vault
(
    id                 UUID                        NOT NULL,
    identifier         VARCHAR(50)                 NOT NULL,
    title              VARCHAR(255)                NOT NULL,
    status             VARCHAR(255)                NOT NULL,
    audio_summary      VARCHAR(255),
    file_link          VARCHAR(255),
    meta_data_id       UUID                        NOT NULL,
    created_by         UUID                        NOT NULL,
    last_modified_by   UUID,
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_data_vault PRIMARY KEY (id)
);

CREATE TABLE metadata
(
    id       UUID         NOT NULL,
    source   VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL,
    category VARCHAR(255),
    edition  VARCHAR(255),
    year     VARCHAR(255) NOT NULL,
    author   VARCHAR(255) NOT NULL,
    CONSTRAINT pk_metadata PRIMARY KEY (id)
);

CREATE TABLE text_chunks
(
    id                 UUID                        NOT NULL,
    title              VARCHAR(255)                NOT NULL,
    content            OID                         NOT NULL,
    transcriber        VARCHAR(255)                NOT NULL,
    audio_link         VARCHAR(255),
    recorder           VARCHAR(255),
    translation_link   VARCHAR(255),
    translator         VARCHAR(255),
    data_vault_id      UUID                        NOT NULL,
    created_by         UUID                        NOT NULL,
    last_modified_by   UUID,
    created_date       TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    last_modified_date TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_text_chunks PRIMARY KEY (id)
);

ALTER TABLE _users
    ADD CONSTRAINT uc__users_email UNIQUE (email);

ALTER TABLE _users
    ADD CONSTRAINT uc__users_username UNIQUE (username);

ALTER TABLE access_token
    ADD CONSTRAINT uc_access_token_user UNIQUE (user_id);

ALTER TABLE data_vault
    ADD CONSTRAINT uc_data_vault_identifier UNIQUE (identifier);

ALTER TABLE data_vault
    ADD CONSTRAINT uc_data_vault_meta_data UNIQUE (meta_data_id);

ALTER TABLE access_token
    ADD CONSTRAINT FK_ACCESS_TOKEN_ON_USER FOREIGN KEY (user_id) REFERENCES _users (id);

ALTER TABLE data_vault
    ADD CONSTRAINT FK_DATA_VAULT_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES _users (id);

ALTER TABLE data_vault
    ADD CONSTRAINT FK_DATA_VAULT_ON_LAST_MODIFIED_BY FOREIGN KEY (last_modified_by) REFERENCES _users (id);

ALTER TABLE data_vault
    ADD CONSTRAINT FK_DATA_VAULT_ON_META_DATA FOREIGN KEY (meta_data_id) REFERENCES metadata (id);

ALTER TABLE text_chunks
    ADD CONSTRAINT FK_TEXT_CHUNKS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES _users (id);

ALTER TABLE text_chunks
    ADD CONSTRAINT FK_TEXT_CHUNKS_ON_DATA_VAULT FOREIGN KEY (data_vault_id) REFERENCES data_vault (id);

CREATE INDEX idx_data_vault_id ON text_chunks (data_vault_id);

ALTER TABLE text_chunks
    ADD CONSTRAINT FK_TEXT_CHUNKS_ON_LAST_MODIFIED_BY FOREIGN KEY (last_modified_by) REFERENCES _users (id);

ALTER TABLE _users
    ADD CONSTRAINT FK__USERS_ON_CREATED_BY FOREIGN KEY (created_by) REFERENCES _users (id);