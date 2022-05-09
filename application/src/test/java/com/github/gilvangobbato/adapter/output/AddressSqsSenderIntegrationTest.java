package com.github.gilvangobbato.adapter.output;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.GetQueueAttributesRequest;
import com.amazonaws.services.sqs.model.GetQueueAttributesResult;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;
import com.github.gilvangobbato.domain.Address;
import com.github.gilvangobbato.port.output.IAddressSqsSender;
import configuration.BaseIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class AddressSqsSenderIntegrationTest extends BaseIntegrationTest {

    private static final String NUMBER_OF_MESSAGES = "ApproximateNumberOfMessages";
    @Autowired
    AmazonSQSAsync amazonSQSAsync;
    @Autowired
    private IAddressSqsSender addressSqsSender;

    @Test
    public void shouldSendMessageSuccessfully() {

        StepVerifier.create(addressSqsSender.send(Address.builder().cep("95720000").build()))
                .assertNext(it -> {
                    assertNotNull(it);
                    final var request = new ReceiveMessageRequest()
                            .withQueueUrl(amazonSQSAsync.getQueueUrl("viaCepInsert").getQueueUrl())
                            .withWaitTimeSeconds(3);
                    final var messageResult = amazonSQSAsync.receiveMessage(request);
                    Assertions.assertNotNull(messageResult);
                    Assertions.assertNotNull(messageResult.getMessages());
                    Assertions.assertEquals(0, messageResult.getMessages().size());
                }).verifyComplete();
    }

    @Test
    public void shouldHaveMessageOnQueue() {
        String queue = "messageToTest";
        String anyBody = "We have a body";
        amazonSQSAsync.sendMessageAsync(amazonSQSAsync.getQueueUrl(queue).getQueueUrl(), anyBody);

        GetQueueAttributesRequest getQueueAttributesRequest
                = new GetQueueAttributesRequest(amazonSQSAsync.getQueueUrl(queue).getQueueUrl())
                .withAttributeNames("All");

        GetQueueAttributesResult getQueueAttributesResult
                = amazonSQSAsync.getQueueAttributes(getQueueAttributesRequest);

        Assertions.assertEquals("1", getQueueAttributesResult.getAttributes()
                .get(NUMBER_OF_MESSAGES));
    }

    @Test
    public void shouldNotHaveAnyMessageOnQueue() {
        GetQueueAttributesRequest getQueueAttributesRequest
                = new GetQueueAttributesRequest(amazonSQSAsync.getQueueUrl("viaCepInsert").getQueueUrl())
                .withAttributeNames("All");

        GetQueueAttributesResult getQueueAttributesResult
                = amazonSQSAsync.getQueueAttributes(getQueueAttributesRequest);

        Assertions.assertEquals("0", getQueueAttributesResult.getAttributes()
                .get("ApproximateNumberOfMessages"));
    }

}