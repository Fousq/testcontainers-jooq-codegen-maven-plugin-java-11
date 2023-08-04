package org.testcontainers.jooq.codegen.database;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.util.Optional;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.containers.PostgreSQLContainer;

/** DatabaseProvider provides container instance for a given DatabaseType */
public class DatabaseProvider {

    /** Instantiates a Docker container using Testcontainers for the given database type. */
    public static JdbcDatabaseContainer<?> getDatabaseContainer(DatabaseProps props) {
        DatabaseType dbType = props.getType();
        String image = Optional.ofNullable(props.getContainerImage()).orElse(dbType.getDefaultImage());
        JdbcDatabaseContainer<?> container = createJdbcDatabaseContainer(dbType, image);

        if (isNotBlank(props.getUsername())) {
            container.withUsername(props.getUsername());
        }
        if (isNotBlank(props.getPassword())) {
            container.withPassword(props.getPassword());
        }
        if (isNotBlank(props.getDatabaseName())) {
            container.withDatabaseName(props.getDatabaseName());
        }
        return container;
    }

    private static JdbcDatabaseContainer<?> createJdbcDatabaseContainer(DatabaseType dbType, String image) {
        switch (dbType) {
            case POSTGRES:
                return new PostgreSQLContainer<>(image);
            case MARIADB:
                return new MariaDBContainer<>(image);
            case MYSQL:
                return new MySQLContainer<>(image);
            default:
                throw new IllegalArgumentException("Unsupported database type " + dbType.name());
        }
    }
}
