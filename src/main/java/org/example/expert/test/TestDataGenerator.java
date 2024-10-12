package org.example.expert.test;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

@RequiredArgsConstructor
public class TestDataGenerator {

    private final JdbcTemplate jdbcTemplate;
    private final TransactionTemplate transactionTemplate;

    public void generateUsers(int count) {
        final int batchSize = 10000;
        final String sql = "INSERT INTO users (email, password, nickname, user_role, created_at, modified_at) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)";

        for (int i = 0; i < count; i += batchSize) {
            final int start = i;
            final int batchCount = Math.min(batchSize, count - i);
            transactionTemplate.execute(status -> {
                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int j) throws SQLException {
                        int userId = start + j;
                        ps.setString(1, "user" + userId + "@example.com");
                        ps.setString(2, "password" + userId);
                        ps.setString(3, "User" + userId);
                        ps.setString(4, "USER");
                    }

                    @Override
                    public int getBatchSize() {
                        return batchCount;
                    }
                });
                return null;
            });
            System.out.println("Inserted " + (i + batchCount) + " users");
        }
    }
}
