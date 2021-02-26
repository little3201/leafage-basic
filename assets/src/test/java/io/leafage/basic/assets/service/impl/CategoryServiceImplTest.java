package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Category;
import io.leafage.basic.assets.repository.CategoryRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Mono;

@SpringBootTest
class CategoryServiceImplTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void getAliasById() {
        Mono<Category> alias = categoryRepository.getById(new ObjectId("5fc88e8a014016148493c5cd"));
        Assertions.assertNotNull(alias);
    }
}