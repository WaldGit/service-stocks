CREATE TABLE stock_investment (
    id UUID PRIMARY KEY,
    ticker VARCHAR(10) NOT NULL,
    current_price DECIMAL(10,2),
    closed BOOLEAN NOT NULL,
    closed_date TIMESTAMP
);

CREATE TABLE stock_tranche (
    id UUID PRIMARY KEY,
    investment_id UUID NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    quantity INT NOT NULL,
    date TIMESTAMP NOT NULL,
    transaction_cost DECIMAL(10,2),
    percentage_gain DECIMAL(5,2),
    FOREIGN KEY (investment_id) REFERENCES stock_investment(id) ON DELETE CASCADE
);
