package com.taskflow.qa.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.DriverManager;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers(disabledWithoutDocker = true)
class PostgresContainerTest {

    @Container
    private static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("taskflow_test")
                    .withUsername("taskflow")
                    .withPassword("taskflow_test");

    @Test
    void startsPostgresAndAcceptsTransactionalQueries() throws Exception {
        try (var connection = DriverManager.getConnection(
                POSTGRES.getJdbcUrl(), POSTGRES.getUsername(), POSTGRES.getPassword());
             var statement = connection.createStatement();
             var result = statement.executeQuery("SELECT 1")) {
            result.next();
            assertEquals(1, result.getInt(1));
        }
    }
}
