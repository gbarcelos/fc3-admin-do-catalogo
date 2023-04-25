package com.fullcycle.admin.catalogo.application;

import com.fullcycle.admin.catalogo.IntegrationTest;
import com.fullcycle.admin.catalogo.application.category.create.CreateCategoryUseCase;
import com.fullcycle.admin.catalogo.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@IntegrationTest
public class SampleIT {

    @Autowired
    private CreateCategoryUseCase createCategoryUseCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjects(){
        Assertions.assertNotNull(createCategoryUseCase);
        Assertions.assertNotNull(categoryRepository);
    }
}
