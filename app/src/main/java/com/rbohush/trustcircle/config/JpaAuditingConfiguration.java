package com.rbohush.trustcircle.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Configuration for Spring Data JPA Auditing.
 * Automatically manages created_at and updated_at timestamps for entities.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfiguration {
}