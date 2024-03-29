package com.fullcycle.admin.catalogo;

import com.fullcycle.admin.catalogo.infrastructure.configuration.WebServerConfig;
import java.lang.annotation.*;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@Tag("integrationTest")
public @interface AmqpTest {}
