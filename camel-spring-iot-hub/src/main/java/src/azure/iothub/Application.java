package src.azure.iothub;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Created by cov-127 on 22/11/16.
 */
@EnableAutoConfiguration
@ComponentScan
@EnableAsync
@EnableScheduling
public class Application {
    public static void main(String[] args) throws Exception {
        new SpringApplicationBuilder(Application.class)
                .showBanner(false)
                .run(args);
    }
}
