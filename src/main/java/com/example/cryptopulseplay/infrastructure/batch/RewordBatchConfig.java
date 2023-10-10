//package com.example.cryptopulseplay.infrastructure.batch;
//
//import com.example.cryptopulseplay.domian.reword.model.Reword;
//import jakarta.persistence.EntityManagerFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.database.JpaItemWriter;
//import org.springframework.batch.item.database.JpaPagingItemReader;
//import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
//import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//
//@Configuration
//@EnableBatchProcessing
//@RequiredArgsConstructor
//public class RewordBatchConfig {
//
//
//    private final JobRepository jobRepository;
//
//    private final PlatformTransactionManager transactionManager;
//
//    private final EntityManagerFactory entityManagerFactory;
//
//
//    @Bean
//    public Job gameResultJob() {
//        return new JobBuilder("rewordJob", jobRepository).start(rewordStep())
//                .build();
//    }
//
//    @Bean
//    public Step rewordStep() {
//        return new StepBuilder("rewordStep", jobRepository)
//                .<Reword, Reword>chunk(100 ,transactionManager)
//                .reader(rewordReader())
//                .processor(rewordProcessor())
//                .writer(rewordWriter())
//                .build();
//    }
//
//
//    @Bean
//    public JpaPagingItemReader<Reword> rewordReader() {
//        return new JpaPagingItemReaderBuilder<Reword>()
//                .name("rewordReader")
//                .entityManagerFactory(entityManagerFactory)
//                .pageSize(100)
//                .queryString("select r from Reword r where r.rewordStatus = 'PENDING'")
//                .build();
//    }
//
//    @Bean
//    public ItemProcessor<Reword, Reword> rewordProcessor() {
//        return reword -> {
//            reword.applyReword();
//            return reword;
//        };
//    }
//
//    @Bean
//    public JpaItemWriter<Reword> rewordWriter() {
//        return new JpaItemWriterBuilder<Reword>()
//                .entityManagerFactory(entityManagerFactory)
//                .build();
//    }
//
//
//
//
//
//}
