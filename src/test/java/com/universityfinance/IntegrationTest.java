package com.universityfinance;

import com.universityfinance.config.AsyncSyncConfiguration;
import com.universityfinance.config.EmbeddedSQL;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(
    classes = { UniversityFinanceApp.class, AsyncSyncConfiguration.class, com.universityfinance.config.JacksonHibernateConfiguration.class }
)
@EmbeddedSQL
public @interface IntegrationTest {}
