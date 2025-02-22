package com.example.pocspringbatch.manager;

import com.example.pocspringbatch.configuration.Constants;
import lombok.AllArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.integration.partition.RemotePartitioningManagerStepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.integration.aws.outbound.SqsMessageHandler;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.json.ObjectToJsonTransformer;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
@Profile("manager")
@AllArgsConstructor
public class ManagerConfiguration {

    private static final int GRID_SIZE = 100;

    private final RemotePartitioningManagerStepBuilderFactory managerStepBuilderFactory;

    @Bean
    public DirectChannel requests() {
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow outboundFlow(SqsAsyncClient sqsAsyncClient) {
        SqsMessageHandler sqsMessageHandler = new SqsMessageHandler(sqsAsyncClient);
        sqsMessageHandler.setQueue(Constants.QUEUE_NAME);
        return IntegrationFlow.from(requests())
                .transform(objectToJsonTransformer())
                .handle(sqsMessageHandler)
                .get();
    }


    @Bean
    public ObjectToJsonTransformer objectToJsonTransformer() {
        return new ObjectToJsonTransformer();
    }

    @Bean
    public Step managerStep() {
        return this.managerStepBuilderFactory.get("managerStep")
                .partitioner("workerStep", new BasicPartitioner())
                .gridSize(GRID_SIZE)
                .outputChannel(requests())
                .build();
    }

    @Bean
    public Job remotePartitioningJob(JobRepository jobRepository) {
        return new JobBuilder("remotePartitioningJob", jobRepository)
                .start(managerStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
}
