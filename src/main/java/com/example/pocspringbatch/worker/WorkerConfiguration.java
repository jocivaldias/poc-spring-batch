package com.example.pocspringbatch.worker;

import com.example.pocspringbatch.configuration.Constants;
import com.example.pocspringbatch.manager.LoggingStepListener;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.integration.partition.RemotePartitioningWorkerStepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.aws.inbound.SqsMessageDrivenChannelAdapter;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.transaction.PlatformTransactionManager;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

import java.util.ArrayList;
import java.util.List;

@Configuration
@AllArgsConstructor
@Profile("worker")
@Slf4j
public class WorkerConfiguration {

    private final RemotePartitioningWorkerStepBuilderFactory workerStepBuilderFactory;

    @Bean
    public IntegrationFlow inboundFlow(SqsAsyncClient sqsAsyncClient) {
        SqsMessageDrivenChannelAdapter sqsMessageDrivenChannelAdapter =
                new SqsMessageDrivenChannelAdapter(sqsAsyncClient, Constants.QUEUE_NAME);

        return IntegrationFlow
                .from(sqsMessageDrivenChannelAdapter)
                .channel(request())
                .get();
    }

    @Bean
    public DirectChannel request() {
        return new DirectChannel();
    }

    @Bean
    public Step workerStep(PlatformTransactionManager transactionManager) {
        return this.workerStepBuilderFactory.get("workerStep")
                .inputChannel(request())
                .<String, String>chunk(100, transactionManager)
                .reader(itemReader(0, 0))
                .processor(itemProcessor())
                .writer(itemWriter())
                .transactionManager(transactionManager)
                .listener(new LoggingStepListener())
                .build();
    }

    @Bean
    @StepScope
    public ListItemReader<String> itemReader(@Value("#{stepExecutionContext['start']}") int start,
                                             @Value("#{stepExecutionContext['end']}") int end) {
        List<String> items = new ArrayList<>();
        for (int i = start; i < end; i++) {
            items.add("Item" + i);
        }
        return new ListItemReader<>(items);
    }

    @Bean
    public ItemProcessor<String, String> itemProcessor() {
        return item -> {
            Thread.sleep(200);
            return item.toUpperCase();
        };
    }

    @Bean
    public ItemWriter<String> itemWriter() {
//        return items -> items.forEach(item -> log.info("Writing item: {}", item));
        return items -> {
            for (String item : items) {
//                log.info("Writing item: {}", item);
            }
        };
    }
}