package io.leafage.basic.assets.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataMongoTest
class PostsRepositoryTest {

    @Autowired
    private PostsRepository postsRepository;

    @Test
    void findByEnabledTrue() {
        Pageable pageable = PageRequest.of(0, 2);
        postsRepository.findByEnabledTrue(pageable);
    }

    @Test
    void findByCategoryIdAndEnabledTrue() {
    }

    @Test
    void getByCodeAndEnabledTrue() {
    }

    @Test
    void countByCategoryIdAndEnabledTrue() {
    }

    @Test
    void findByIdGreaterThanAndEnabledTrue() {
    }

    @Test
    void findByIdLessThanAndEnabledTrue() {
    }

    @Test
    void findAllBy() {
    }
}