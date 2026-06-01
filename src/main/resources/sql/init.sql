CREATE TABLE account (
    id BIGSERIAL PRIMARY KEY,
    number VARCHAR(20) NOT NULL UNIQUE,
    type VARCHAR(20) NOT NULL,
    initial_balance NUMERIC(15,2) NOT NULL,
    balance NUMERIC(15,2) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    modified_at TIMESTAMP,
    customer_id BIGSERIAL NOT NULL,
    CONSTRAINT fk_account_customer
        FOREIGN KEY (customer_id)
        REFERENCES customer(id)
);
CREATE INDEX account_customer_id_idx ON account (customer_id);

CREATE TABLE movement (
    id BIGSERIAL PRIMARY KEY,
    movement_date TIMESTAMP NOT NULL,
    type VARCHAR(20) NOT NULL,
    amount NUMERIC(15,2) NOT NULL,
    previous_balance NUMERIC(15,2) NOT NULL,
    current_balance NUMERIC(15,2) NOT NULL,
    status BOOLEAN NOT NULL DEFAULT TRUE,
    account_id BIGINT NOT NULL,
    CONSTRAINT fk_movement_account
        FOREIGN KEY (account_id)
        REFERENCES account(id)
);
CREATE INDEX movement_account_id_idx ON movement (account_id);
CREATE INDEX movement_movement_date_idx ON movement (movement_date);