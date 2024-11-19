package com.bitesaitzz.QuickPost.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "com.bitesaitzz.QuickPost.repositories.jpa")
public class JpaConfig {
}
