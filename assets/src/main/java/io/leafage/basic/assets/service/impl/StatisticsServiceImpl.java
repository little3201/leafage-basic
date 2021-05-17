package io.leafage.basic.assets.service.impl;

import io.leafage.basic.assets.document.Posts;
import io.leafage.basic.assets.document.Statistics;
import io.leafage.basic.assets.repository.PostsRepository;
import io.leafage.basic.assets.repository.StatisticsRepository;
import io.leafage.basic.assets.service.StatisticsService;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class StatisticsServiceImpl implements StatisticsService {

    private final PostsRepository postsRepository;

    private final StatisticsRepository statisticsRepository;

    public StatisticsServiceImpl(PostsRepository postsRepository, StatisticsRepository statisticsRepository) {
        this.postsRepository = postsRepository;
        this.statisticsRepository = statisticsRepository;
    }

    @Override
    public void viewedStatistics() {
        Posts posts = new Posts();
        posts.setEnabled(true);
        AtomicLong viewed = new AtomicLong();
        AtomicLong likes = new AtomicLong();
        AtomicLong comment = new AtomicLong();
        postsRepository.findAll(Example.of(posts)).toStream().forEach((p -> {
            viewed.getAndAdd(p.getViewed());
            likes.getAndAdd(p.getLikes());
            comment.getAndAdd(p.getComment());
        }));
        Statistics statistics = new Statistics(LocalDate.now().toEpochDay(), viewed.get(), likes.get(), comment.get());
        statisticsRepository.save(statistics);
    }
}
