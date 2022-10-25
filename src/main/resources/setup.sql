CREATE TABLE IF NOT EXISTS player_cash
(
    uuid CHAR(36) NOT NULL PRIMARY KEY,
    cash INT NOT NULL,
);

CREATE TABLE IF NOT EXISTS player_cash_log
(
    id INT NOT NULL PRIMARY KEY,
    uuid CHAR(36) NOT NULL,
    amount INT NOT NULL,
    status CHAR(20) NOT NULL,
    manager CHAR(36) NOT NULL,
    product VARCHAR NULL,
    reg_date DATE NOT NULL,
);