package com.fullcycle.admin.catalogo;

import java.lang.annotation.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@ComponentScan(
    includeFilters = {@ComponentScan.Filter(type = FilterType.REGEX, pattern = ".[MySQLGateway]")})
@DataJpaTest
@ExtendWith(CleanUpExtension.class)
public @interface MySQLGatewayTest {}
