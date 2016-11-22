package src.azure.iothub;

import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.amqp.AMQPComponent;
import org.apache.camel.component.jms.JmsConfiguration;
import org.apache.camel.spring.SpringCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Hashtable;

/**
 * Created by cov-127 on 22/11/16.
 */
@Configuration
public class AzureIOTHubConfig {
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AzureRouteBuilder routeBuilder;

    @Value("${servicebus.propertyFile.path:/home/cov-127/servicebus_new.properties}")
    private String servicebusPropertyFilePath;

    @Bean(name = "UriTemplate")
    public String uriTemplate() throws Exception {
        return "amqp:queue:$queueName";
    }

    @Bean
    public ConnectionFactory jmsConnectionFactory(){
        Hashtable<String, String> env = new Hashtable<>();
        env.put(Context.INITIAL_CONTEXT_FACTORY,
                "org.apache.qpid.amqp_1_0.jms.jndi.PropertiesFileInitialContextFactory");
        env.put(Context.PROVIDER_URL, servicebusPropertyFilePath);
        Context context = null;
        ConnectionFactory cf = null;
        try {
            context = new InitialContext(env);
            // Look up ConnectionFactory and Queue
            cf = (ConnectionFactory) context.lookup("SBCF");
            Destination rawParameterQueue = (Destination) context.lookup("RAW_PARAMETER_QUEUE");
        } catch (NamingException e) {
            e.printStackTrace();
        }
        return cf;
    }

    @Bean(name="jmsConfig")
    public JmsConfiguration jmsConfig(){
        JmsConfiguration configuration = new JmsConfiguration();
        configuration.setConnectionFactory(jmsConnectionFactory());
        configuration.setCacheLevelName("CACHE_CONSUMER");
        return configuration;
    }


    @Bean
    public AMQPComponent amqpComponent(){
        AMQPComponent component = new AMQPComponent();
        component.setConfiguration(jmsConfig());
        return  component;
    }

    @Bean
    public SpringCamelContext camelContext(AMQPComponent component, ConnectionFactory connectionFactory) throws Exception {
        SpringCamelContext camelContext = new SpringCamelContext(applicationContext);
        component.setCamelContext(camelContext);
        component.setConnectionFactory(connectionFactory);
        component.start();
        addRoutes(camelContext);
        return camelContext;
    }

    private void addRoutes(CamelContext camelContext) throws Exception {
        camelContext.addRoutes(routeBuilder);
    }

    @Bean
    public ProducerTemplate producerTemplate(CamelContext context) {
        return context.createProducerTemplate();
    }
}
