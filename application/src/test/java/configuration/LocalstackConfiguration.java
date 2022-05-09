package configuration;

import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.SetQueueAttributesRequest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cloud.aws.messaging.config.annotation.EnableSqs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;

@EnableSqs
@TestConfiguration
@TestPropertySource("classpath:application.yml")
public class LocalstackConfiguration {

    private static final String LOCALSTACK_ACCOUNT = "000000000000";
    public static final String LOCALSTACK_ACCOUNT_NUMBER_SLASH_SORROUNDED = "/" +
            LocalstackConfiguration.LOCALSTACK_ACCOUNT + "/";
    private static final String QUEUE_ARN = "QueueArn";

    @Bean
    @Primary
    public AmazonSQSAsync amazonSQSAsync() {
        AmazonSQSAsync amazonSQSAsync = AmazonSQSAsyncClientBuilder
                .standard()
                .withEndpointConfiguration(
                        BaseIntegrationTest.LOCAL_STACK_CONTAINER.getEndpointConfiguration(LocalStackContainer.Service.SQS))
                .withCredentials(BaseIntegrationTest.LOCAL_STACK_CONTAINER.getDefaultCredentialsProvider())
                .build();

        createSqsQueue(amazonSQSAsync, "viaCepInsert");
        createSqsQueue(amazonSQSAsync, "messageToTest");
        return amazonSQSAsync;
    }

    private void createSqsQueue(AmazonSQS amazonSqs, String sqsQueueName) {
        amazonSqs.createQueue(sqsQueueName);
        String dlqName = sqsQueueName + "-DLQ";
        amazonSqs.createQueue(dlqName);

        String dl_queue_url = amazonSqs
                .getQueueUrl(dlqName)
                .getQueueUrl();

        GetQueueAttributesResult queue_attrs = amazonSqs
                .getQueueAttributes(
                        new GetQueueAttributesRequest(dl_queue_url)
                                .withAttributeNames(QUEUE_ARN));

        String dl_queue_arn = queue_attrs.getAttributes().get(QUEUE_ARN);
        String src_queue_url = amazonSqs.getQueueUrl(sqsQueueName)
                .getQueueUrl();

        SetQueueAttributesRequest request = new SetQueueAttributesRequest()
                .withQueueUrl(src_queue_url)
                .addAttributesEntry("VisibilityTimeout", "5")
                .addAttributesEntry("RedrivePolicy",
                        "{\"maxReceiveCount\":\"1\", \"deadLetterTargetArn\":\""
                                + dl_queue_arn + "\"}");

        amazonSqs.setQueueAttributes(request);
    }

}
