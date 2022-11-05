CREATE TABLE IF NOT EXISTS player_cash
(
    uuid CHAR(36) NOT NULL PRIMARY KEY,
    cash INT NOT NULL
);

CREATE TABLE IF NOT EXISTS player_cash_log
(
    id INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    uuid CHAR(36) NOT NULL,
    amount INT NOT NULL,
    cash_charge_type CHAR(20) NOT NULL,
    log_status CHAR(20) NOT NULL,
    manager CHAR(36) NOT NULL,
    product VARCHAR NULL,
    reg_date DATE NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_player_cash_log_by_uuid ON player_cash_log (uuid);

CREATE INDEX idx_player_cash_log_by_status ON player_cash_log (cash_charge_type);

CREATE INDEX idx_player_cash_log_by_status ON player_cash_log (log_status);

CREATE INDEX idx_player_cash_bought_log ON player_cash_log (product);