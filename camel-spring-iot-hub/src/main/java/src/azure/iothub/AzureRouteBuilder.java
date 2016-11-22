package src.azure.iothub;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by vivek on 22/11/16.
 */
@Component
public class AzureRouteBuilder extends RouteBuilder {
    @Resource(name="UriTemplate")
    private String uriTemplate;

    @Value("${queue.name:{}/ConsumerGroups/$default/Partitions/0}")
    private String queue_1;

    @Autowired
    private ApplicationContext appContext;

    @Override
    public void configure() throws Exception {
        from(uriTemplate.replace("$queueName", queue_1)).streamCaching()
                .process(appContext.getBean(AzureIOTHubProcessor.class));
    }
}
