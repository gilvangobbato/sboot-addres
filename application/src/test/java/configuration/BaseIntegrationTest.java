package configuration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.localstack.LocalStackContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@SpringBootTest
@Import({LocalstackConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BaseIntegrationTest {

    @Container
    protected static final LocalStackContainer LOCAL_STACK_CONTAINER =
            new LocalStackContainer(DockerImageName.parse("localstack/localstack:0.13.3"))
                    .withServices(LocalStackContainer.Service.SQS);

    public static String getLocalStackHost(){
        return "http://".concat(LOCAL_STACK_CONTAINER.getHost()).concat(":")
                .concat(LOCAL_STACK_CONTAINER.getFirstMappedPort().toString());
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry){
        registry.add("aws.region", LOCAL_STACK_CONTAINER::getRegion);
        registry.add("aws.sqs.endpoint", BaseIntegrationTest::getLocalStackHost);
    }
}
