package src.azure.iothub;

import org.apache.camel.AsyncCallback;
import org.apache.camel.AsyncProcessor;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

/**
 * Created by cov-127 on 22/11/16.
 */
@Component
public class AzureIOTHubProcessor implements AsyncProcessor {
    @Override
    public boolean process(Exchange exchange, AsyncCallback callback) {
        String message = exchange.getIn().getBody(String.class);
        System.out.println("Message Received: "+ message);
        callback.done(true);
        return true;
    }

    @Override
    public void process(Exchange exchange) throws Exception {

    }
}
