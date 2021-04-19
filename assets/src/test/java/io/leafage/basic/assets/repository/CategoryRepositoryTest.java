package io.leafage.basic.assets.repository;

import io.leafage.basic.assets.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findByCodeAndEnabledTrue() {
        Category category = new Category();
        category.setCode("1234");
        category.setEnabled(true);
        category.setModifyTime(LocalDateTime.now());
        this.entityManager.persist(category);
        category = this.categoryRepository.findByCodeAndEnabledTrue("1234");
        assertThat(category.getCode()).isEqualTo("1234");
    }

    @Test
    void findByCodeInAndEnabledTrue() {
        Category category = new Category();
        category.setCode("1234");
        category.setEnabled(true);
        category.setModifyTime(LocalDateTime.now());
        this.entityManager.persist(category);
        List<Category> categories = this.categoryRepository.findByCodeInAndEnabledTrue(Collections.singleton("1234"));
        assertThat(categories.get(0).getCode()).isEqualTo("1234");
    }
}