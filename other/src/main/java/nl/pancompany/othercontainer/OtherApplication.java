package nl.pancompany.othercontainer;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.admin.SpringApplicationAdminJmxAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:context.properties")
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class, SpringApplicationAdminJmxAutoConfiguration.class })
public class OtherApplication {

    static void main(String[] args) {
        new SpringApplicationBuilder(OtherApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }

}
