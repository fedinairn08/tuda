package com.tuda;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.Duration;

@Testcontainers
public abstract class AbstractIntegrationTest {

    private static final PostgreSQLContainer<?> POSTGRES;

    private static final GenericContainer<?> MINIO;

    static {
        POSTGRES = new PostgreSQLContainer<>("postgres:15-alpine")
                .withStartupTimeout(Duration.ofMinutes(3))
                .withConnectTimeoutSeconds(120)
                .withDatabaseName("tuda")
                .withUsername("root")
                .withPassword("root")
                .withReuse(true)
                .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));
        POSTGRES.start();

        MINIO = new GenericContainer<>("minio/minio")
                .withExposedPorts(9000)
                .withEnv("MINIO_ROOT_USER", "root")
                .withEnv("MINIO_ROOT_PASSWORD", "minio123")
                .withCommand("server /data")
                .waitingFor(Wait.forHttp("/minio/health/ready").forPort(9000));
        MINIO.start();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);

        registry.add("s3.url", () -> "http://" + MINIO.getHost() + ":" + MINIO.getMappedPort(9000));
        registry.add("s3.accessKey", () -> "root");
        registry.add("s3.secretKey", () -> "minio123");
        registry.add("s3.bucket", () -> "tuda");
    }
}
