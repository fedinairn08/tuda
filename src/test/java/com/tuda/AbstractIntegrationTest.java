package com.tuda;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public abstract class AbstractIntegrationTest {

    @Container
    static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("tuda")
            .withUsername("root")
            .withPassword("root");

    @Container
    static final GenericContainer<?> minio = new GenericContainer<>("minio/minio")
            .withExposedPorts(9000)
            .withEnv("MINIO_ROOT_USER", "root")
            .withEnv("MINIO_ROOT_PASSWORD", "minio123")
            .withCommand("server /data")
            .waitingFor(Wait.forHttp("/minio/health/ready").forPort(9000));

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("s3.url", () -> "http://" + minio.getHost() + ":" + minio.getMappedPort(9000));
        registry.add("s3.accessKey", () -> "root");
        registry.add("s3.secretKey", () -> "minio123");
        registry.add("s3.bucket", () -> "tuda");
    }
}
