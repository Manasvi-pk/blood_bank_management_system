-- Run this script against the blood_donor_management database with MySQL 8.0.
-- It creates missing tables and updates existing columns without dropping tables.
-- Back up the database before running any schema migration.

CREATE TABLE IF NOT EXISTS donors (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    blood_group VARCHAR(3),
    email VARCHAR(150),
    area VARCHAR(150),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS donations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    donor_id BIGINT NULL,
    donor_name VARCHAR(100),
    blood_group VARCHAR(3),
    units_donated INT,
    donation_date DATE,
    status VARCHAR(30) DEFAULT 'Completed',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (donor_id) REFERENCES donors(id)
);

CREATE TABLE IF NOT EXISTS blood_inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    blood_group VARCHAR(3) UNIQUE,
    units_available INT DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS blood_requests (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    recipient_name VARCHAR(100),
    blood_group VARCHAR(3),
    units_requested INT,
    hospital VARCHAR(150),
    request_date DATE,
    status VARCHAR(30) DEFAULT 'Pending',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    contact_person VARCHAR(100),
    priority VARCHAR(20) NOT NULL DEFAULT 'Normal'
);

-- Add every required column that may be absent from an older schema.
DELIMITER //
DROP PROCEDURE IF EXISTS ensure_blood_bank_column//
CREATE PROCEDURE ensure_blood_bank_column(
    IN p_table_name VARCHAR(64),
    IN p_column_name VARCHAR(64),
    IN p_column_definition TEXT
)
BEGIN
    DECLARE column_exists INT DEFAULT 0;

    SELECT COUNT(*) INTO column_exists
    FROM information_schema.COLUMNS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = p_table_name
      AND COLUMN_NAME = p_column_name;

    IF column_exists = 0 THEN
        SET @add_column_sql = CONCAT(
            'ALTER TABLE `', p_table_name, '` ADD COLUMN `',
            p_column_name, '` ', p_column_definition
        );
        PREPARE add_column_statement FROM @add_column_sql;
        EXECUTE add_column_statement;
        DEALLOCATE PREPARE add_column_statement;
    END IF;
END//
DELIMITER ;

CALL ensure_blood_bank_column('donors', 'name', 'VARCHAR(100)');
CALL ensure_blood_bank_column('donors', 'blood_group', 'VARCHAR(3)');
CALL ensure_blood_bank_column('donors', 'email', 'VARCHAR(150)');
CALL ensure_blood_bank_column('donors', 'area', 'VARCHAR(150)');
CALL ensure_blood_bank_column('donors', 'created_at', 'TIMESTAMP DEFAULT CURRENT_TIMESTAMP');

CALL ensure_blood_bank_column('donations', 'donor_id', 'BIGINT NULL');
CALL ensure_blood_bank_column('donations', 'donor_name', 'VARCHAR(100)');
CALL ensure_blood_bank_column('donations', 'blood_group', 'VARCHAR(3)');
CALL ensure_blood_bank_column('donations', 'units_donated', 'INT');
CALL ensure_blood_bank_column('donations', 'donation_date', 'DATE');
CALL ensure_blood_bank_column('donations', 'status', 'VARCHAR(30) DEFAULT ''Completed''');
CALL ensure_blood_bank_column('donations', 'created_at', 'TIMESTAMP DEFAULT CURRENT_TIMESTAMP');

CALL ensure_blood_bank_column('blood_inventory', 'blood_group', 'VARCHAR(3)');
CALL ensure_blood_bank_column('blood_inventory', 'units_available', 'INT DEFAULT 0');
CALL ensure_blood_bank_column('blood_inventory', 'last_updated', 'TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP');

CALL ensure_blood_bank_column('blood_requests', 'recipient_name', 'VARCHAR(100)');
CALL ensure_blood_bank_column('blood_requests', 'blood_group', 'VARCHAR(3)');
CALL ensure_blood_bank_column('blood_requests', 'units_requested', 'INT');
CALL ensure_blood_bank_column('blood_requests', 'hospital', 'VARCHAR(150)');
CALL ensure_blood_bank_column('blood_requests', 'request_date', 'DATE');
CALL ensure_blood_bank_column('blood_requests', 'status', 'VARCHAR(30) DEFAULT ''Pending''');
CALL ensure_blood_bank_column('blood_requests', 'created_at', 'TIMESTAMP DEFAULT CURRENT_TIMESTAMP');
CALL ensure_blood_bank_column('blood_requests', 'contact_person', 'VARCHAR(100)');
CALL ensure_blood_bank_column('blood_requests', 'priority', 'VARCHAR(20) NOT NULL DEFAULT ''Normal''');

DROP PROCEDURE ensure_blood_bank_column;

ALTER TABLE donors
    MODIFY COLUMN name VARCHAR(100) NULL,
    MODIFY COLUMN blood_group VARCHAR(3) NULL,
    MODIFY COLUMN email VARCHAR(150) NULL,
    MODIFY COLUMN area VARCHAR(150) NULL;

ALTER TABLE donations
    MODIFY COLUMN donor_id BIGINT NULL,
    MODIFY COLUMN donor_name VARCHAR(100) NULL,
    MODIFY COLUMN blood_group VARCHAR(3) NULL,
    MODIFY COLUMN units_donated INT NULL,
    MODIFY COLUMN donation_date DATE NULL,
    MODIFY COLUMN status VARCHAR(30) NULL DEFAULT 'Completed';

ALTER TABLE blood_inventory
    MODIFY COLUMN blood_group VARCHAR(3) NULL,
    MODIFY COLUMN units_available INT NULL DEFAULT 0,
    MODIFY COLUMN last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP;

ALTER TABLE blood_requests
    MODIFY COLUMN recipient_name VARCHAR(100) NULL,
    MODIFY COLUMN blood_group VARCHAR(3) NULL,
    MODIFY COLUMN units_requested INT NULL,
    MODIFY COLUMN hospital VARCHAR(150) NULL,
    MODIFY COLUMN request_date DATE NULL,
    MODIFY COLUMN status VARCHAR(30) NULL DEFAULT 'Pending',
    MODIFY COLUMN contact_person VARCHAR(100) NULL,
    MODIFY COLUMN priority VARCHAR(20) NOT NULL DEFAULT 'Normal';

-- Preserve the required one-row-per-blood-group rule for existing databases.
SET @inventory_unique_key_exists := (
    SELECT COUNT(*)
    FROM information_schema.STATISTICS
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'blood_inventory'
      AND COLUMN_NAME = 'blood_group'
      AND NON_UNIQUE = 0
);
SET @add_inventory_unique_key := IF(
    @inventory_unique_key_exists = 0,
    'ALTER TABLE blood_inventory ADD CONSTRAINT uq_blood_inventory_blood_group UNIQUE (blood_group)',
    'SELECT 1'
);
PREPARE add_inventory_unique_key FROM @add_inventory_unique_key;
EXECUTE add_inventory_unique_key;
DEALLOCATE PREPARE add_inventory_unique_key;

-- Add the donor foreign key only when an equivalent relationship is absent.
SET @donor_foreign_key_exists := (
    SELECT COUNT(*)
    FROM information_schema.KEY_COLUMN_USAGE
    WHERE TABLE_SCHEMA = DATABASE()
      AND TABLE_NAME = 'donations'
      AND COLUMN_NAME = 'donor_id'
      AND REFERENCED_TABLE_NAME = 'donors'
      AND REFERENCED_COLUMN_NAME = 'id'
);
SET @add_donor_foreign_key := IF(
    @donor_foreign_key_exists = 0,
    'ALTER TABLE donations ADD CONSTRAINT fk_donations_donor FOREIGN KEY (donor_id) REFERENCES donors(id)',
    'SELECT 1'
);
PREPARE add_donor_foreign_key FROM @add_donor_foreign_key;
EXECUTE add_donor_foreign_key;
DEALLOCATE PREPARE add_donor_foreign_key;
