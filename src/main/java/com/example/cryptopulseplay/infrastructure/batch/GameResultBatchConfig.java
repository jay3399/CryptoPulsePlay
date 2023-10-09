package com.example.cryptopulseplay.infrastructure.batch;

import com.example.cryptopulseplay.domian.game.model.Game;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class GameResultBatchConfig {

    private final JobRepository jobRepository;
    private final RedisGameItemReader redisGameItemReader;
    private final GameItemProcessor gameItemProcessor;
    private final GameItemWriter gameItemWriter;

    private final PlatformTransactionManager platformTransactionManager;


    @Bean
    public Job gameResultJob() {
        return new JobBuilder("resultJob", jobRepository).start(gameResultCalculationStep())
                .build();
    }

    @Bean
    public Step gameResultCalculationStep() {
        return new StepBuilder("gameResultCalculationStep", jobRepository)
                .<Game, Game>chunk(100 ,platformTransactionManager)
                .reader(redisGameItemReader)
                .processor(gameItemProcessor)
                .writer(gameItemWriter)
                .build();
    }


}
