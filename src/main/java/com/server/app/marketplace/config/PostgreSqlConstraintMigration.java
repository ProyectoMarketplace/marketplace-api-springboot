package com.server.app.marketplace.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostgreSqlConstraintMigration {

    private final JdbcTemplate jdbcTemplate;
    private final Environment environment;

    @EventListener(ApplicationReadyEvent.class)
    public void updatePaymentStatusConstraint() {
        String datasourceUrl = environment.getProperty("spring.datasource.url", "");
        if (!datasourceUrl.contains("postgresql")) {
            return;
        }

        jdbcTemplate.execute("ALTER TABLE payments DROP CONSTRAINT IF EXISTS payments_status_check");
        jdbcTemplate.execute("""
                ALTER TABLE payments ADD CONSTRAINT payments_status_check
                CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'REFUNDED', 'PARTIALLY_REFUNDED'))
                """);
    }
}
