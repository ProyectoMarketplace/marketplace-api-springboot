-- Ejecutar en marketplace_db si falla el reembolso con:
-- "payments_status_check" al usar REFUNDED o PARTIALLY_REFUNDED

ALTER TABLE payments DROP CONSTRAINT IF EXISTS payments_status_check;

ALTER TABLE payments ADD CONSTRAINT payments_status_check
    CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'REFUNDED', 'PARTIALLY_REFUNDED'));
