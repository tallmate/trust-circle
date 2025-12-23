import org.jooq.meta.jaxb.ForcedType
import org.jooq.meta.jaxb.Logging

plugins {
    id("org.springframework.boot")
    id("org.flywaydb.flyway")
    id("nu.studer.jooq")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":repository"))
    implementation(project(":service"))
    implementation(project(":api"))

    // Spring Boot starters
    implementation(libs.spring.boot.starter.web)
    implementation(libs.spring.boot.starter.data.jpa)
    implementation(libs.spring.boot.starter.security)
    implementation(libs.spring.boot.starter.actuator)
    implementation(libs.spring.boot.starter.jooq)

    // Flyway for database migrations
    implementation(libs.flyway.core)
    implementation(libs.flyway.database.postgresql)

    // PostgreSQL driver (needed at runtime and for Flyway/jOOQ)
    implementation(libs.postgresql)

    // jOOQ code generator (only needed at build time)
    jooqGenerator(libs.postgresql)
}

// Flyway configuration
flyway {
    url = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/trustcircle"
    user = System.getenv("DB_USERNAME") ?: "postgres"
    password = System.getenv("DB_PASSWORD") ?: "postgres"
    locations = arrayOf("filesystem:src/main/resources/db/migration")
    schemas = arrayOf("public")
}

// jOOQ code generation configuration
jooq {
    // Match Spring Boot's jOOQ version
    version.set(libs.versions.jooq.get())

    configurations {
        create("main") {
            jooqConfiguration.apply {
                logging = Logging.WARN

                jdbc.apply {
                    driver = "org.postgresql.Driver"
                    url = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/trustcircle"
                    user = System.getenv("DB_USERNAME") ?: "postgres"
                    password = System.getenv("DB_PASSWORD") ?: "postgres"
                }

                generator.apply {
                    name = "org.jooq.codegen.DefaultGenerator"

                    database.apply {
                        name = "org.jooq.meta.postgres.PostgresDatabase"
                        inputSchema = "public"
                        excludes = "flyway_schema_history"

                        // Map PostgreSQL types to Java types
                        forcedTypes.addAll(listOf(
                            ForcedType().apply {
                                userType = "java.time.Instant"
                                includeTypes = "TIMESTAMP.*"
                            }
                        ))
                    }

                    target.apply {
                        packageName = "com.rbohush.trustcircle.jooq"
                        directory = "build/generated-src/jooq/main"
                    }

                    generate.apply {
                        isDeprecated = false
                        isRecords = true
                        isImmutablePojos = true
                        isFluentSetters = true
                    }
                }
            }
        }
    }
}

// Generate jOOQ classes after Flyway migration
// Note: Run manually with: ./gradlew :app:flywayMigrate :app:generateJooq
tasks.named("generateJooq") {
    // Only depend on flywayMigrate if explicitly requested
    // This prevents automatic database connection during normal builds
    if (project.gradle.startParameter.taskNames.any { it.contains("generateJooq") || it.contains("flywayMigrate") }) {
        dependsOn("flywayMigrate")
    }

    // Inputs for up-to-date checking
    inputs.files(fileTree("src/main/resources/db/migration"))
        .withPropertyName("migrations")
        .withPathSensitivity(PathSensitivity.RELATIVE)

    // Skip if not explicitly requested
    onlyIf {
        project.gradle.startParameter.taskNames.any { it.contains("generateJooq") }
    }
}

tasks.named<org.springframework.boot.gradle.tasks.bundling.BootJar>("bootJar") {
    archiveFileName.set("app.jar")
    mainClass.set("com.rbohush.trustcircle.Application")
}